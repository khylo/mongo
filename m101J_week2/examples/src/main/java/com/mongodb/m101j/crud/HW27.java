/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.m101j.crud;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.m101j.util.Helpers.printJson;

public class HW27 {
	public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("video");
        MongoCollection<Document> collection = database.getCollection("testMovie");
        
        long count = collection.count();
        System.out.println();
        System.out.println("Orig count "+count);

//        Bson filter = new Document("x", 0)
//        .append("y", new Document("$gt", 10).append("$lt", 90));

        Bson filter = eq("awards.oscars.award", "bestPicture");
        Bson projection = Projections.fields(Projections.include("title", "year"), Projections.excludeId());

        List<Document> all = collection.find(filter).projection(projection).into(new ArrayList<Document>());
        System.out.println("Filtered count "+all.size());
        System.out.println("Filtered output (Best Picture winners) "+all);
        

        //count = collection.count();
        //System.out.println("Deleting "+del);
        //System.out.println("New count "+count);
    }
}
