package com.projecta;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedEvaluate {
	public static void main(String[] args) throws Exception {
		DataModel model = new FileDataModel(new File("/Users/huanli/documents/cs/239/project1/cmpe239-project-A/movienight_2.csv"));
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder builder = new ItemRecommenderBuilder();
		double sum = 0;
		int n = 500;
		for (int i = 0; i < n; i++) {
			sum += evaluator.evaluate(builder, null, model, 0.9, 1.0);
		}
		System.out.println("RMSE of item based recommendation: " + sum / n);
	}
}

class ItemRecommenderBuilder implements RecommenderBuilder {
	public Recommender buildRecommender(DataModel model) throws TasteException {
		ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model, similarity);
		Recommender cachingRecommender = new CachingRecommender(recommender);
		return cachingRecommender;
	}
}
