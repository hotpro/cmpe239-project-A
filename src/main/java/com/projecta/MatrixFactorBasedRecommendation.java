package com.projecta;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogManager;

import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.SparkConf;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import scala.Tuple2;

public class MatrixFactorBasedRecommendation {
	public static void main(String[] args) {
		Logger.getLogger("org").setLevel(Level.OFF);
	    
	    //Set user and number of recommended items:
	    final int user = 20;
	    int itemNumber = 10;
	    
	    // Change to your local path
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

	    // Build the recommendation model using ALS
	    int rank = 10;
	    int numIterations = 10;
	    MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, 0.01);
	    
	    //Get the set of movies the user already watched
	    JavaRDD<Integer> watchedMoviesRDD = ratings.filter(
	    	new Function<Rating, Boolean>() {
	    		public Boolean call(Rating r) throws Exception {
	    			if (r.user() == user)	return true;
	    			return false;
	    		}
	    	}).map(
		    	new Function<Rating, Integer>() {
			        public Integer call(Rating r) throws Exception {
			            return r.product();
			        }
		    	});
	    Set<Integer> watchedMovies = new HashSet<Integer>(watchedMoviesRDD.collect());
	    
	    //Print recommended movies
	    Rating[] rate = model.recommendProducts(user,27);
	    for (Rating r : rate) {
	    	if (itemNumber == 0)	break;
	    	if(!watchedMovies.contains(r.product())) {
	    		itemNumber--;
	    		System.out.println("User: " + r.user() + ", Product: " + r.product() + ", Rating: " + r.rating());
	    	}
	    }
	}
}
