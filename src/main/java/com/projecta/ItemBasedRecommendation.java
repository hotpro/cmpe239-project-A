package com.projecta;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;
import java.util.List;

public class ItemBasedRecommendation {
	
	private static final long USER_ID = 37L;
	
	private static final int RECOMMENDED_ITEM_NUM = 3;

	public static void main(String[] args) {
		try {
			DataModel model = new FileDataModel(new File("movienight_2.csv"));
			//Collection<GenericItemSimilarity.ItemItemSimilarity> correlations = new ArrayList<GenericItemSimilarity.ItemItemSimilarity>();
			//ItemSimilarity itemSimilarity = new GenericItemSimilarity(correlations);
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			List<RecommendedItem> recommendations = cachingRecommender.recommend(USER_ID, RECOMMENDED_ITEM_NUM);
			List<String> movies = Utils.getMovieList();
			for (RecommendedItem item : recommendations) {
				System.out.print(item);
				System.out.print(movies.get((int)item.getItemID()));
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

}
