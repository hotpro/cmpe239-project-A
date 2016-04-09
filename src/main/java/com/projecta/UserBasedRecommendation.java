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

public class UserBasedRecommendation {

	public static void main(String[] args) {
		try {
			DataModel model = new FileDataModel(new File("/Users/xiaofengli/Documents/CMPE239/cmpe239-project-A/movienight_2.csv"));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			List<RecommendedItem> recommendations = recommender.recommend(2, 4);
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
			
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
