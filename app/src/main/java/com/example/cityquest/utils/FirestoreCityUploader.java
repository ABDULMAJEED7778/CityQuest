package com.example.cityquest.utils;

import android.util.Log;

import com.example.cityquest.model.City;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirestoreCityUploader {

    // Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Upload the list of cities to Firestore
    public void uploadCities() {
        // List of cities
        List<City> cities = new ArrayList<>();

        cities.add(new City("Bangalore", "India", "Modern", "ChIJbU60yXAWrjsR4E9-UejD3_g", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.8", "A vibrant city known for its technology industry and parks.","Embark on a journey through the Cultural Delights of Bangalore, where vibrant traditions meet modern innovation. Explore bustling markets, savor aromatic spices, and immerse yourself in the rich heritage of India's Garden City."));

        cities.add(new City("Delhi", "India", "Historical", "ChIJLbZ-NFv9DDkRQJY4FbcFcgM", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "The capital city of India, known for its rich history and culture.","Embark on a journey through the Historic Wonders of Delhi, where ancient monuments meet the vibrant pulse of modern India. Wander through bustling markets, admire majestic Mughal architecture, and savor the flavors of the capital city."));

        cities.add(new City("Mumbai", "India", "Modern", "ChIJwe1EZjDG5zsRaYxkjY_tpF0", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India.","Dive into the Dynamic Spirit of Mumbai, the City of Dreams, where Bollywood glamour meets iconic landmarks. Stroll along the Marine Drive, explore the Gateway of India, and experience the unmatched energy of this metropolis."));

        cities.add(new City("Chennai", "India", "Coastal", "ChIJYTN9T-plUjoRM9RjaAunYW4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.6", "A coastal city famous for its cultural heritage and temples.","Discover the Coastal Charms of Chennai, a city of classical music and temple art. Feel the sea breeze on Marina Beach, marvel at Dravidian architecture, and immerse yourself in the cultural rhythm of South India."));

        cities.add(new City("Hyderabad", "India", "Historical", "ChIJx9Lr6tqZyzsRwvu6koO3k64", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.4", "Known for its iconic Charminar and rich history.","Experience the Timeless Splendor of Hyderabad, where history and culture intertwine. Visit the iconic Charminar, taste world-famous biryani, and delve into the legacy of the Nizams."));

        cities.add(new City("Kolkata", "India", "Cultural", "ChIJZ_YISduC-DkRvCxsj-Yw40M", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.3", "A city with rich cultural heritage and colonial architecture.","Step into the Cultural Capital of Kolkata, where art and heritage define every corner. Admire the colonial architecture, enjoy literary festivals, and savor the city’s famous sweets."));

        cities.add(new City("Pune", "India", "Historical", "ChIJARFGZy6_wjsRQ-Oenb9DjYI", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.5", "Known for its historical significance and academic institutions.","Explore the Academic and Cultural Heart of Pune, where historic forts meet vibrant student life. Relish its pleasant weather, delve into its Maratha legacy, and experience its lively café culture."));

        cities.add(new City("Jaipur", "India", "Historical", "ChIJgeJXTN9KbDkRCS7yDDrG4Qw", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.7", "The 'Pink City' known for its royal palaces and forts.","Immerse yourself in the Royal Grandeur of Jaipur, the Pink City of India. Marvel at opulent palaces, explore bustling bazaars, and uncover tales of the Rajput kings."));

        cities.add(new City("Ahmedabad", "India", "Cultural", "ChIJSdRbuoqEXjkRFmVPYRHdzk8", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.2", "Known for its textile industry and historic attractions.","Discover the Heritage Treasures of Ahmedabad, a city of textiles, temples, and timeless architecture. Walk along the Sabarmati River and explore the intricate beauty of its stepwells."));

        cities.add(new City("Surat", "India", "Modern", "ChIJYxUdQVlO4DsRQrA4CSlYRf4", "https://images.pexels.com/photos/4888907/pexels-photo-4888907.jpeg", "4.1","Famous for its diamond industry and riverside view.", "Unveil the Sparkling Charm of Surat, renowned for its diamond industry and vibrant textiles. Stroll along the riverside, explore bustling markets, and experience the city's entrepreneurial spirit."));

        cities.add(new City("Agra", "India", "Historical", "ChIJ2UEvfIUNdDkRQjtSqTjvSng", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.9","Home to the iconic Taj Mahal, a historical wonder." ,"Step into the Timeless Beauty of Agra, home to the iconic Taj Mahal. Explore Mughal-era wonders, enjoy serene gardens, and immerse yourself in the romance of history."));

        cities.add(new City("Lucknow", "India", "Cultural", "ChIJa7EyH5n9mzkR54uXCYm6zJM", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.4","Known for its Nawabi culture and Mughal history.", "Experience the Nawabi Elegance of Lucknow, where culture and hospitality shine. Relish royal Awadhi cuisine, admire intricate architecture, and wander through its bustling bazaars."));

        cities.add(new City("Goa", "India", "Coastal", "ChIJQbc2YxC6vzsRkkDzYv-H-Oo", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.8", "A popular coastal destination known for its beaches and nightlife.","Soak in the Vibrant Vibes of Goa, India’s coastal paradise. Enjoy golden beaches, explore charming churches, and experience a nightlife like no other."));

        cities.add(new City("Amritsar", "India", "Historical", "ChIJVXOeVqpkGTkRfe-E7ltgou4", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.7", "Known for the Golden Temple and its spiritual significance.","Journey to the Spiritual Heart of Varanasi, one of the oldest cities in the world. Witness sacred rituals on the ghats, explore narrow alleys, and feel the divine aura of the Ganges."));

        cities.add(new City("Varanasi", "India", "Cultural", "ChIJTc_rb7ctjjkRtfA_hRAXE2g", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.6", "One of the world's oldest cities, famous for its spiritual heritage.","Journey to the Spiritual Heart of Varanasi, one of the world's oldest cities. Witness sacred rituals on the ghats, explore narrow alleys, and feel the divine aura of the Ganges."));

        cities.add(new City("Udaipur", "India", "Historical", "ChIJEUShUGXlZzkRsGi4VYTC2Ns", "https://images.pexels.com/photos/2402164/pexels-photo-2402164.jpeg", "4.9", "Known as the 'City of Lakes' and renowned for its royal palaces.","Step into the Regal Splendor of Udaipur, the 'City of Lakes.' Marvel at the stunning palaces, tranquil waters, and timeless charm of this royal destination."));

        cities.add(new City("Thiruvananthapuram", "India", "Coastal", "ChIJR827Bbi7BTsRy4FcXKufQxU", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.3", "The capital of Kerala, known for its coastal charm and temples.","Immerse yourself in the Coastal Serenity of Thiruvananthapuram. Discover its golden beaches, ancient temples, and vibrant cultural scene."));

        cities.add(new City("Madurai", "India", "Historical", "ChIJM5YYsYLFADsRMzn2ZHJbldw", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.5", "An ancient city known for the Meenakshi Temple and cultural heritage.","Explore the Sacred Heritage of Madurai, home to the iconic Meenakshi Temple. Experience its spiritual energy and deep cultural roots."));

        cities.add(new City("Mysore", "India", "Cultural", "ChIJ-S5XHThwrzsRbTn4wOjsiSs", "https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg", "4.7", "Known for its royal heritage and palaces.","Explore the Sacred Heritage of Madurai, home to the iconic Meenakshi Temple. Experience its spiritual energy and deep cultural roots."));

        cities.add(new City("Coimbatore", "India", "Modern", "ChIJtRyXL69ZqDsRgtI-GB7IwS8", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "4.4", "A rapidly growing industrial city with a modern touch.", "Uncover the Industrial Pulse of Coimbatore, a city blending modern advancements with timeless traditions and scenic surroundings."));

        cities.add(new City("Shimla", "India", "Coastal", "ChIJZ25d4-N4BTkRt1Sf__Z_fh8", "https://images.pexels.com/photos/4169734/pexels-photo-4169734.jpeg", "4.8", "A beautiful hill station known for its colonial architecture.","Escape to the Scenic Retreat of Shimla, a hill station adorned with colonial architecture and breathtaking Himalayan views."));

        cities.add(new City("Rishikesh", "India", "Cultural", "ChIJEfGTz2c-CTkR_huUb0qAeMw", "https://images.pexels.com/photos/1054289/pexels-photo-1054289.jpeg", "4.9", "Known as the 'Yoga Capital of the World' and for its spiritual vibe.","Discover the Spiritual and Adventurous Essence of Rishikesh, known as the 'Yoga Capital of the World' and gateway to the Himalayas."));

        cities.add(new City("Bhopal", "India", "Historical", "ChIJvY_Wj49CfDkR-NRy1RZXFQI", "https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg", "4.5", "Known for its historical significance and ancient monuments.","Experience the Historical and Modern Duality of Bhopal, a city rich with ancient monuments and thriving urban life."));

        cities.add(new City("Guwahati", "India", "Cultural", "ChIJ_zORfyhaWjcRMt5rQzITvSs", "https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg", "4.6", "Known for its natural beauty and cultural festivals.","Celebrate the Cultural Mosaic of Guwahati, where natural beauty meets vibrant festivals and a deep spiritual heritage."));


        // Upload each city to Firestore
        for (City city : cities) {
            db.collection("cities").document(city.getCityId()).set(city)
                    .addOnSuccessListener(aVoid -> {
                        // Log success or notify user
                        Log.e("City " ,city.getCityName() + " added successfully!");

                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e("City ","Error adding city: " + e.getMessage());
                    });
        }
    }
}
