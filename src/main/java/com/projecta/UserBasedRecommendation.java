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

			// Step 1:- Input CSV file (CSV file should be in userID, itemID,
			// preference) format

			// DataModel dm = new FileDataModel(new File("filePath"));

			// Step 2:- Create UserSimilarity or ItemSimilarity Matrix

			// Step 3:- Create UserNeighbourHood object. (No Need to create
			// ItemNeighbourHood object while creating
			// Item based Recommendation)

			// Step 4:- Create object of UserBasedRecommender or
			// ItemBasedRecommender

			// Step 5:- Call the Generated Recommender in previous step to
			// getting
			// recommendation for particular user or Item
			
			
			
			DataModel model = new FileDataModel(new File("/Users/xiaofengli/documents/cmpe239/lab1/lab1.csv"));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			List<RecommendedItem> recommendations = recommender.recommend(2, 3);
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
			
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
