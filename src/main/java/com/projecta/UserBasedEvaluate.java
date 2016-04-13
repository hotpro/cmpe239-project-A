package com.projecta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class UserBasedEvaluate {
	public static void main(String[] args) throws Exception {
		//RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("movienight_2.csv"));
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder builder = new UserRecommenderBuilder();
		double sum = 0;
		int n = 500;
		for (int i = 0; i < n; i++) {
			sum += evaluator.evaluate(builder, null, model, 0.9, 1.0);
		}
		System.out.println("RMSE of user based recommendation: " + sum / n);
	}
}

class UserRecommenderBuilder implements RecommenderBuilder {
	public Recommender buildRecommender(DataModel model) throws TasteException {
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
		return new GenericUserBasedRecommender(model, neighborhood, similarity);
	}
}