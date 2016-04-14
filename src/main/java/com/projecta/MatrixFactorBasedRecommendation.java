package com.projecta;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.*;
import org.apache.spark.*;
import org.apache.spark.mllib.recommendation.Rating;

import java.util.*;

import scala.Tuple2;

import org.apache.spark.api.java.function.*;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.ALS;

public class MatrixFactorBasedRecommendation {

    static SparkConf conf;
    static JavaSparkContext sc;
    static int userId = 10;
    static int itemNum = 5;

    public static void main(String args[]) {
    	Logger.getLogger("org").setLevel(Level.OFF);
    	
    	//Initializing Spark
        conf = new SparkConf().setAppName("MovieRecommendation").setMaster("local");
        sc = new JavaSparkContext(conf);

        //Reading Data
        final JavaRDD<String> ratingData = sc.textFile("/Users/huanli/documents/cs/239/project1/cmpe239-project-A/matrixFactorData.csv");
	    JavaRDD<String> productData = sc.textFile("/Users/huanli/documents/cs/239/project1/cmpe239-project-A/movies.csv");


	    //Ratings file should be csv file in a (UserID, MovieID, Rating,timestamp) Format
	    // assign some random Integer in timestamp filed (timestamp will be used to divide data in training, test and validation dataset)
	    //Keep this block as it is
	    JavaRDD<Tuple2<Integer, Rating>> ratings = ratingData.map(
            new Function<String, Tuple2<Integer, Rating>>() {
                public Tuple2<Integer, Rating> call(String s) throws Exception {
                    String[] row = s.split(",");
                    Integer cacheStamp = Integer.parseInt(row[3]) % 10;
                    Rating rating = new Rating(Integer.parseInt(row[0]), Integer.parseInt(row[1]), Double.parseDouble(row[2]));
                    return new Tuple2<Integer, Rating>(cacheStamp, rating);
                }
            }
	    );

	    //Movies file should be csv file in a (MovieID,Title) format
	    //Keep this block as it is
	    Map<Integer, String> products = productData.mapToPair(
            new PairFunction<String, Integer, String>() {
                public Tuple2<Integer, String> call(String s) throws Exception {
                    String[] sarray = s.split(",");
                    return new Tuple2<Integer, String>(Integer.parseInt(sarray[0]), sarray[1]);
                }
            }
	    ).collectAsMap();


	    //training data set
	    // below function generate training data from input data 
	    // keep other things as it is
	    JavaRDD<Rating> training = ratings.filter(
            new Function<Tuple2<Integer, Rating>, Boolean>() {
                public Boolean call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._1() < 6;
                    // write your logic to create training data set based on timestamp from input dataset
                }
            }
	    ).map(
            new Function<Tuple2<Integer, Rating>, Rating>() {
                public Rating call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._2();
                }
            }
	    ).cache();


	    //validation data set
	    // below function generate validation data from input data 
	    // keep other things as it is
	    JavaRDD<Rating> validation = ratings.filter(
            new Function<Tuple2<Integer, Rating>, Boolean>() {
                public Boolean call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._1() >= 6 && tuple._1() < 8;
                    
                }
            }
	    ).map(
            new Function<Tuple2<Integer, Rating>, Rating>() {
                public Rating call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._2();
                }
            }
	    );

	    //test data set
	    // below function generate validation data from input data 
	    // keep other things as it is
	    JavaRDD<Rating> test = ratings.filter(
            new Function<Tuple2<Integer, Rating>, Boolean>() {
                public Boolean call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._1() >= 8;
                   
                }
            }
	    ).map(
            new Function<Tuple2<Integer, Rating>, Rating>() {
                public Rating call(Tuple2<Integer, Rating> tuple) throws Exception {
                    return tuple._2();
                }
            }
	    );

	    //###########   Implement below part    #################//
	    //train the model with sparks ALS.train method
	    //ALS.train method takes 4 input parameters :- (training model, rank, lambda , numofIterations)
	    // try different value of rank, lambda and numofIterations and test accuracy of model everytime
	    // to pick the model with highest accuracy
	
	    // To calculate accuracy, write your logic in computeAccuracy model provided below
	
	
	    //After creating tarining model call getRecommendation model and print recommendationd for particular user
	    // getRecommendation model will take 4 input paramaeters (userId, trained model,All user Ratings, All Movies)
	    // Implement Get recommendation method below
	   
	    System.out.println("Validating model ...");
	    
	    double minRMSE = Double.MAX_VALUE;
	    int bestRank = 0;
	    int bestNumIter = 0;
	    double bestLambda = 0;
	    MatrixFactorizationModel bestModel = null;
	    
	    for (int rank = 5; rank < 10; rank += 2) {
	        for (int numIter = 8; numIter <= 12; numIter += 2) {
	            for (double lambda = 0.01; lambda < 0.9; lambda += 0.2) {
	                MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(training), rank, numIter, lambda);
	                Double RMSE = computeAccuracy(model, validation);
	                if (RMSE < minRMSE) {
	                    bestModel = model;
	                    minRMSE = RMSE;
	                    bestRank = rank;
	                    bestLambda = lambda;
	                    bestNumIter = numIter;
	                }
	            }
	        }
	    }
	    
	    System.out.println("The smallest RMSE is " + minRMSE + " when rank = " + bestRank + ", numIter = " 
	    		+ bestNumIter + ", lambda = " + bestLambda);
	    System.out.println("Recommended movies for user " + userId + ": " );
	    List<Rating> items = getRecommendations(userId, bestModel,ratings, products);
	    for (Rating i : items) {
	    	System.out.println(i);
	    }

}

    public static Double computeAccuracy(MatrixFactorizationModel model, JavaRDD<Rating> data) {
    	JavaRDD<Tuple2<Object, Object>> userProducts = data.map(
    		new Function<Rating, Tuple2<Object, Object>>() {
    			public Tuple2<Object, Object> call(Rating r) {
    				return new Tuple2<Object, Object>(r.user(), r.product());
    			}
    		}
    	);
    		    
    	JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
    		model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD().map(
    		new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
    			public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r){
    				return new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating());
    		    }
    		}
    	));
    	JavaRDD<Tuple2<Double, Double>> ratesAndPreds =
    		JavaPairRDD.fromJavaRDD(data.map(
    			new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
    				public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r){
    					return new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating());
    		        }
    		    }
    		)).join(predictions).values();
    	double MSE = JavaDoubleRDD.fromRDD(ratesAndPreds.map(
    		new Function<Tuple2<Double, Double>, Object>() {
    			public Object call(Tuple2<Double, Double> pair) {
    				Double err = pair._1() - pair._2();
    				return err * err;
    		    }
    		}).rdd()).mean();
    	return Math.sqrt(MSE);
    }
    
    private static List<Rating> getRecommendations(final int userId, MatrixFactorizationModel model, JavaRDD<Tuple2<Integer, Rating>> ratings, Map<Integer, String> products) {
            List<Rating> recommendations;

            //###########   Implement below part    #################//

            // your logic for generating movie recommendation for particular user
            // Use methods provided by spark to generate recommendation for particular user


            //Getting the users ratings
            JavaRDD<Rating> userRatings = ratings.filter(
            	new Function<Tuple2<Integer, Rating>, Boolean>() {
            		public Boolean call(Tuple2<Integer, Rating> tuple) throws Exception {
            			return tuple._2().user() == userId;
                    }
                }
            ).map(
                new Function<Tuple2<Integer, Rating>, Rating>() {
                	public Rating call(Tuple2<Integer, Rating> tuple) throws Exception {
                		return tuple._2();
                	}
                });

            //Getting the product ID's of the products that user rated
            JavaRDD<Tuple2<Object, Object>> userProducts = userRatings.map(
                    new Function<Rating, Tuple2<Object, Object>>() {
                        public Tuple2<Object, Object> call(Rating r) {
                            return new Tuple2<Object, Object>(r.user(), r.product());
                        }
                    }
            );

            List<Integer> productSet = new ArrayList<Integer>();
            productSet.addAll(products.keySet());

            Iterator<Tuple2<Object, Object>> productIterator = userProducts.toLocalIterator();

            //Removing the user watched (rated) set from the all product set
            while(productIterator.hasNext()) {
                Integer movieId = (Integer)productIterator.next()._2();
                if(productSet.contains(movieId)){
                    productSet.remove(movieId);
                }
            }

            JavaRDD<Integer> candidates = sc.parallelize(productSet);

            JavaRDD<Tuple2<Integer, Integer>> userCandidates = candidates.map(
                    new Function<Integer, Tuple2<Integer, Integer>>() {
                        public Tuple2<Integer, Integer> call(Integer integer) throws Exception {
                            return new Tuple2<Integer, Integer>(userId, integer);
                        }
                    }
            );

            //Predict recommendations for the given user
            recommendations = model.predict(JavaPairRDD.fromJavaRDD(userCandidates)).collect();

            //Sorting the recommended products and sort them according to the rating
            Collections.sort(recommendations, new Comparator<Rating>() {
                public int compare(Rating r1, Rating r2) {
                    return r1.rating() < r2.rating() ? 1 : r1.rating() > r2.rating() ? -1 : 0;
                }
            });

            //get top 5 from the recommended products.
            recommendations = recommendations.subList(0, itemNum);

            return recommendations;
        }


}