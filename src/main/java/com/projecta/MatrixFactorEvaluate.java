package com.projecta;

import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.SparkConf;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.mahout.common.RandomUtils;

import scala.Tuple2;

public class MatrixFactorEvaluate {
	private static double lambda;
	private static int rank;
	private static int numIterations;
	
	public static void main(String[] args) {
		lambda = 0.1;
		rank = 5;
		numIterations = 8;
		
		//RandomUtils.useTestSeed();
		Logger.getLogger("org").setLevel(Level.OFF);
		String path = "/Users/huanli/documents/cs/239/project1/cmpe239-project-A/movienight_2.csv";
		SparkConf conf = new SparkConf().setAppName("Matrix Factor Based Recommendation").setMaster("local[2]");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<String> data = jsc.textFile(path);
	    JavaRDD<Rating> ratings = data.map(
	      new Function<String, Rating>() {
	        public Rating call(String s) {
	          String[] sarray = s.split(",");
	          return new Rating(Integer.parseInt(sarray[0]), Integer.parseInt(sarray[1]),
	            Double.parseDouble(sarray[2]));
	        }
	      }
	    );
	    while (lambda < 1) {
	    	System.out.println("lambda=" + lambda + ", " + getRMSE(ratings));
	    	lambda += 0.1;
	    }
	}
	
	public static double getRMSE(JavaRDD<Rating> ratings) {
	    JavaRDD<Rating>[] splits = ratings.randomSplit(new double[]{0.9, 0.1}, 1000);
	    JavaRDD<Rating> training = splits[0];
	    JavaRDD<Rating> test = splits[1];
	    
	    //int rank = 10;
	    //int numIterations = 10;
	    MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(training), rank, numIterations, lambda, 1, 2000);
	    
	    JavaRDD<Tuple2<Object, Object>> userProducts = test.map(
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
	      JavaPairRDD.fromJavaRDD(test.map(
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
	      }
	    ).rdd()).mean();
	    return Math.sqrt(MSE);
	}
}
