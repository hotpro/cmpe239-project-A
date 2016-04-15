package com.projecta;

import java.io.File;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class UserBasedEvaluate {
	
	private static double threshold;
	
	public static void main(String[] args) throws Exception {
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("movienight_2.csv"));
		threshold = 0.1;
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder builder = new UserRecommenderBuilder();
		// Calculate the relation between RMSE and threshold
		while (threshold < 1) {
			System.out.println("threshold = " + threshold + ", RMSE = " + evaluator.evaluate(builder, null, model, 0.9, 1));
			threshold += 0.1;
		}
	}
	
	public static class UserRecommenderBuilder implements RecommenderBuilder {
		public Recommender buildRecommender(DataModel model) throws TasteException {
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
			return new GenericUserBasedRecommender(model, neighborhood, similarity);
		}
	}
}