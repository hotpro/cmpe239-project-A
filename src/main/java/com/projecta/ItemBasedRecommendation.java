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
	
	private static final long USER_ID = 6L;
	
	private static final int RECOMMENDED_ITEM_NUM = 3;

	public static void main(String[] args) {
		try {
			//Create the data model object.
			DataModel model = new FileDataModel(new File("movienight_2.csv"));
			//Create the pearson similarity.
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			//Generate the recommender object.
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);
			//Caching the recommender object.
			Recommender cachingRecommender = new CachingRecommender(recommender);
			//Get the recommendation items.
			List<RecommendedItem> recommendations = cachingRecommender.recommend(USER_ID, RECOMMENDED_ITEM_NUM);
			//Iterate the recommended items.
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
		} catch (Exception e) {
			//If there is any exception, just print the exception msg.
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
