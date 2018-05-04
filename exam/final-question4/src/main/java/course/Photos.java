package course;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

public class Photos {
	/**
	 * For this we need to look at each image and then see if it is in any of the albums
	 * To do this we will create a multiKey index on albums
	 * db.albums.createIndex({"images":1}
	 * @param args
	 */
        public static void main(String[] args) {
            MongoClient c =  new MongoClient();
            MongoDatabase db = c.getDatabase("photo");
            MongoCollection<Document> imagesDb = db.getCollection("images");
            MongoCollection<Document> albumsDb = db.getCollection("albums");
            //Map<String, Boolean> lookedUpImages = new HashMap<String, Boolean>();
            Set<Integer> toDelete = new HashSet<Integer>();
            
            //Before
            Bson sunrises = Filters.eq("tags", "sunrises");
            long before = imagesDb.count(sunrises);
            System.out.println("Sunrises before delete "+before);
            
           MongoCursor<Document> images = imagesDb.find().projection(Projections.include("_id")).iterator();
           int i=0;
           while(images.hasNext()) {
        	   if(i++%10000==0)
        		   System.out.println("toDelete "+toDelete.size());
        	   Integer imageId = images.next().getInteger("_id");
        	  // if(lookedUpImages.get(imageId)==null) {
    		   Document search = new Document("images", imageId);
    		   boolean orphan = albumsDb.find(search).first()==null;
    		   if(orphan)
    			   toDelete.add(imageId); 
    		//   lookedUpImages.put(imageId, inAlbum);
           }
           // Delete
           Bson deleteDoc = Filters.in("_id", toDelete); //toDelete.stream().collect(Collectors.toMap( x -> "images", x-> x)));
           System.out.println("About to Delete "+toDelete.stream().map (x -> x.toString ()).collect (Collectors.joining (",")));
           imagesDb.deleteMany(deleteDoc);

           long after = imagesDb.count(sunrises);
           System.out.println("Sunrises after delete "+after);
         }
}