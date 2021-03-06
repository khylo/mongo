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
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.m101j.util.Helpers.printJson;

public class HW23 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> collection = database.getCollection("grades");
        
        long count = collection.count();
        System.out.println();
        System.out.println("Orig count "+count);

//        Bson filter = new Document("x", 0)
//        .append("y", new Document("$gt", 10).append("$lt", 90));

        Bson filter = eq("type", "homework"); //and(eq("type", "homework"), gte("score", 65));
        Bson sort1 = Sorts.ascending("student_id");
        Bson sort2 = Sorts.ascending("score");
        Bson sort3 = Sorts.ascending("student_id","score");

        List<Document> all = collection.find(filter).sort(sort3).into(new ArrayList<Document>());
        System.out.println("Filtered count "+all.size());
        
        boolean looping = true;
        int i =0;
        int del = 0;
        Object student=null;
        while(looping && i<all.size()) {
        	Document d =all.get(i);
        	if(!d.get("student_id").equals(student)) {
        		// New student, so delete.
        		student = d.get("student_id");
        		ObjectId id = (ObjectId)d.get("_id");
        		//System.out.println(del+" :"+d);
        		collection.deleteOne(eq("_id",id));
        		del++;
        	}
        	i++;
        }


        count = collection.count();
        System.out.println("Deleting "+del);
        System.out.println("New count "+count);
    }
}
