package com.projecta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class ItemBasedRecommendation {

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
			Collection<GenericItemSimilarity.ItemItemSimilarity> correlations = new ArrayList<GenericItemSimilarity.ItemItemSimilarity>();
			ItemSimilarity itemSimilarity = new GenericItemSimilarity(
					correlations);
			Recommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			List<RecommendedItem> recommendations = cachingRecommender.recommend(2, 1);
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
