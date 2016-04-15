package com.projecta;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;

public class HybridEvaluate {
    private static final long USER_ID = 37L;

    private static final int RECOMMENDED_ITEM_NUM = 3;

    public static void main(String[] args) {
//        hybridItem();
        hybridUserBased();

    }

    private static void hybridItem() {
        DataModel model = null;
        try {
            model = new FileDataModel(new File("movienight_2.csv"));
            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
            DataModel similarityDataModel = new FileDataModel((new File("movie-night-genre-similarity.csv")));
            RecommenderBuilder builder = new ItemRecommenderBuilder(similarityDataModel);
            double sum = 0;
            int n = 500;
            for (int i = 0; i < n; i++) {
                sum += evaluator.evaluate(builder, null, model, 0.9, 1.0);
            }
            System.out.println("RMSE of item based recommendation: " + sum / n);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class ItemRecommenderBuilder implements RecommenderBuilder {

        private DataModel similarityDataModel;

        public ItemRecommenderBuilder(DataModel similarityDataModel) {
            this.similarityDataModel = similarityDataModel;
        }

        public Recommender buildRecommender(DataModel model) throws TasteException {
//            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
            ItemSimilarity similarity = new LogLikelihoodSimilarity(similarityDataModel);
            Recommender recommender = new GenericItemBasedRecommender(model, similarity);
            Recommender cachingRecommender = new CachingRecommender(recommender);
            return cachingRecommender;
        }
    }

    private static void hybridUserBased() {
        DataModel model = null;
        try {
            model = new FileDataModel(new File("movienight_2.csv"));
            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
            DataModel similarityDataModel = new FileDataModel((new File("movie-night-user-genre-similarity.csv")));
            RecommenderBuilder builder = new UserBasedRecommenderBuilder(similarityDataModel);
            double sum = 0;
            int n = 500;
            for (int i = 0; i < n; i++) {
                sum += evaluator.evaluate(builder, null, model, 0.9, 1.0);
            }
            System.out.println("RMSE of item based recommendation: " + sum / n);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class UserBasedRecommenderBuilder implements RecommenderBuilder {

        private DataModel similarityDataModel;

        public UserBasedRecommenderBuilder(DataModel similarityDataModel) {
            this.similarityDataModel = similarityDataModel;
        }

        public Recommender buildRecommender(DataModel model) throws TasteException {
            UserSimilarity similarity = new LogLikelihoodSimilarity(similarityDataModel);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            return recommender;
        }
    }

}
