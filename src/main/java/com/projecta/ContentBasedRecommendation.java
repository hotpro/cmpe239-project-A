package com.projecta;

import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ContentBasedRecommendation {
	private static final int RECOMMENDED_ITEM_NUM = 3;
	private static Logger logger = LoggerFactory.getLogger(ContentBasedRecommendation.class);
	public static void main(String[] args) {
		userBase(799, 137, "3/1/2016 19:07:47,799,20-30,Male");
		userBase(554, 139, "3/1/2016 19:07:58,554,20-30,Male");
	}

	private static void userBase(int sid, int userId, String user) {
		List<String> movies = Utils.getMovieList();
		DataModel model = null;
		try {
			model = new FileDataModel(new File("movie-night-content-base-" + sid + ".csv"));
			UserSimilarity similarity = new LogLikelihoodSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			long[] items = recommender.mostSimilarUserIDs(userId, 4);
			logger.info("=========================={}=============================", user);
			logger.info("movies: {}", Arrays.toString(items));
			for (long m : items) {
				logger.info(movies.get((int)m));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * doest work
	 */
	private static void booleanPref() {
		int n = 10;
		List<String> movies = Utils.getMovieList();
		DataModel model = null;
		try {
			DataModel fileModel = new FileDataModel(new File("movie-night-content-base-799.csv"));
			model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(fileModel));

			UserSimilarity similarity = new LogLikelihoodSimilarity(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(n, Double.NEGATIVE_INFINITY,
					similarity, model, 1.0);
			Recommender recommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
			List<RecommendedItem> recommendations = recommender.recommend(137, RECOMMENDED_ITEM_NUM);
			for (RecommendedItem item : recommendations) {
				System.out.println(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
