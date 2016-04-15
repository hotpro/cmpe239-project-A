package com.projecta;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class UserBasedRecommendation {
	
	private static final long USER_ID = 6L;
	
	private static final int RECOMMENDED_ITEM_NUM = 3;

	public static void main(String[] args) {
		try {
			//fix the the test set
			RandomUtils.useTestSeed();
			//Load the data model from the prepared data file.
			DataModel model = new FileDataModel(new File("movienight_2.csv"));
			//Create a pearson similarity
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			//Create user neighborhood
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
			//Generate recommender according to model, neighborhood and similarity
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			//Get the recommendations of specific user
			List<RecommendedItem> recommendations = recommender.recommend(USER_ID, RECOMMENDED_ITEM_NUM);
			//Iterate the recommended item
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
			
		} catch (Exception e) {
			//Whenn catch exception just print the exception info.
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
