package org.socialmaps.sdk.android;

import mockwebserver3.MockResponse;
import mockwebserver3.RecordedRequest;
import mockwebserver3.junit4.MockWebServerRule;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class SocialMapsUnitTest {
    @Rule
    public final MockWebServerRule serverRule = new MockWebServerRule();

    @Test
    public void retrievePlace() throws Exception {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .body("""
                            {
                              "object": "place",
                              "id": 4442,
                              "name": "Izz Cafe",
                              "location": {
                                "lat": 51.8952597,
                                "lon": -8.4715779
                              },
                              "rating_stats": {
                                "count": 0,
                                "like_ratio": null,
                                "score": 0.5
                              },
                              "osm_type": "N",
                              "osm_id": 7095470096,
                              "osm_tags": {
                                "addr:city": "Cork",
                                "addr:housenumber": "14",
                                "addr:postcode": "T12 EY24",
                                "addr:street": "George's Quay",
                                "amenity": "restaurant",
                                "cuisine": "palestinian",
                                "entrance": "main",
                                "name": "Izz Cafe",
                                "note": "Called \\"cafe\\" but self-identifies and functions as a restaurant",
                                "opening_hours": "We 12:00-20:00; Th-Sa 12:00 21:00; Su 12:00-18:00",
                                "phone": "+353 21 229 0689",
                                "takeaway": "yes",
                                "website": "https://izz.ie/",
                                "wheelchair": "yes"
                              }
                            }
                        """)
                .build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        Place place = socialMaps.retrievePlace(4442L);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/places/4442", request.getUrl().encodedPath());

        // Assert: Response
        assertNotNull(place);
        assertEquals(ObjectType.PLACE, place.object);
        assertEquals(Long.valueOf(4442), place.id);
        assertEquals("Izz Cafe", place.name);
        assertEquals(Double.valueOf(51.8952597), place.location.lat);
        assertEquals(Double.valueOf(-8.4715779), place.location.lon);
        assertEquals(Long.valueOf(0), place.ratingStats.count);
        assertNull(place.ratingStats.likeRatio);
        assertEquals(Double.valueOf(0.5), place.ratingStats.score);
        assertEquals(OsmType.NODE, place.osmType);
        assertEquals(Long.valueOf(7095470096L), place.osmId);
        assertEquals("Cork", place.osmTags.get("addr:city"));
        assertEquals("restaurant", place.osmTags.get("amenity"));
        assertEquals("yes", place.osmTags.get("wheelchair"));
        assertEquals(14, place.osmTags.size());
    }

    @Test
    public void lookupPlace() throws Exception {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .body("""
                            {
                              "object": "place",
                              "id": 4442,
                              "name": "Izz Cafe",
                              "location": {
                                "lat": 51.8952597,
                                "lon": -8.4715779
                              },
                              "rating_stats": {
                                "count": 0,
                                "like_ratio": null,
                                "score": 0.5
                              },
                              "osm_type": "N",
                              "osm_id": 7095470096,
                              "osm_tags": {
                                "addr:city": "Cork",
                                "addr:housenumber": "14",
                                "addr:postcode": "T12 EY24",
                                "addr:street": "George's Quay",
                                "amenity": "restaurant",
                                "cuisine": "palestinian",
                                "entrance": "main",
                                "name": "Izz Cafe",
                                "note": "Called \\"cafe\\" but self-identifies and functions as a restaurant",
                                "opening_hours": "We 12:00-20:00; Th-Sa 12:00 21:00; Su 12:00-18:00",
                                "phone": "+353 21 229 0689",
                                "takeaway": "yes",
                                "website": "https://izz.ie/",
                                "wheelchair": "yes"
                              }
                            }
                        """)
                .build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        Place place = socialMaps.lookupPlace("Izz Cafe", 51.89525, -8.47157);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/places/lookup", request.getUrl().encodedPath());
        assertEquals("Izz Cafe", request.getUrl().queryParameter("name"));
        assertEquals("51.89525", request.getUrl().queryParameter("lat"));
        assertEquals("-8.47157", request.getUrl().queryParameter("lon"));

        // Assert: Response
        assertNotNull(place);
        assertEquals(ObjectType.PLACE, place.object);
        assertEquals(Long.valueOf(4442), place.id);
        assertEquals("Izz Cafe", place.name);
        assertEquals(Double.valueOf(51.8952597), place.location.lat);
        assertEquals(Double.valueOf(-8.4715779), place.location.lon);
        assertEquals(Long.valueOf(0), place.ratingStats.count);
        assertNull(place.ratingStats.likeRatio);
        assertEquals(Double.valueOf(0.5), place.ratingStats.score);
        assertEquals(OsmType.NODE, place.osmType);
        assertEquals(Long.valueOf(7095470096L), place.osmId);
        assertEquals("Cork", place.osmTags.get("addr:city"));
        assertEquals("restaurant", place.osmTags.get("amenity"));
        assertEquals("yes", place.osmTags.get("wheelchair"));
        assertEquals(14, place.osmTags.size());
    }

    @Test
    public void queryPlaces() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(200).body("""
                {
                  "object": "list",
                  "data": [
                    {
                      "object": "place",
                      "id": 4442,
                      "name": "Izz Cafe",
                      "location": { "lat": 51.8952597, "lon": -8.4715779 },
                      "rating_stats": { "count": 0, "like_ratio": null, "score": 0.5 },
                      "osm_type": "N",
                      "osm_id": 7095470096,
                      "osm_tags": {
                        "addr:city": "Cork",
                        "addr:housenumber": "14",
                        "addr:postcode": "T12 EY24",
                        "addr:street": "George's Quay",
                        "amenity": "restaurant",
                        "cuisine": "palestinian",
                        "entrance": "main",
                        "name": "Izz Cafe",
                        "note": "Called \\"cafe\\" but self-identifies and functions as a restaurant",
                        "opening_hours": "We 12:00-20:00; Th-Sa 12:00 21:00; Su 12:00-18:00",
                        "phone": "+353 21 229 0689",
                        "takeaway": "yes",
                        "website": "https://izz.ie/",
                        "wheelchair": "yes"
                      }
                    },
                    {
                      "object": "place",
                      "id": 4453,
                      "name": "Quay Co-op Restaurant",
                      "location": { "lat": 51.8955476, "lon": -8.4750127 },
                      "rating_stats": { "count": 0, "like_ratio": null, "score": 0.5 },
                      "osm_type": "N",
                      "osm_id": 291560343,
                      "osm_tags": {
                        "addr:city": "Cork",
                        "addr:housenumber": "24",
                        "addr:street": "Sullivan's Quay",
                        "amenity": "restaurant",
                        "diet:vegan": "yes",
                        "diet:vegetarian": "yes",
                        "entrance": "main",
                        "name": "Quay Co-op Restaurant",
                        "opening_hours": "Mo-Sa 08:30-18:00",
                        "outdoor_seating": "no",
                        "phone": "+353-21-431-7026",
                        "website": "https://www.quaycoop.com/restaurant/",
                        "wheelchair": "no"
                      }
                    }
                  ]
                }
                """).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        List<Place> places = socialMaps.queryPlaces(
                51.896329601499986,
                51.89393706305316,
                -8.469486410785976,
                -8.47822537992883,
                "$.amenity == \"restaurant\""
        );

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/places", request.getUrl().encodedPath());
        assertEquals("51.896329601499986", request.getUrl().queryParameter("max_lat"));
        assertEquals("51.89393706305316", request.getUrl().queryParameter("max_lon"));
        assertEquals("-8.469486410785976", request.getUrl().queryParameter("min_lat"));
        assertEquals("-8.47822537992883", request.getUrl().queryParameter("min_lon"));
        assertEquals("$.amenity == \"restaurant\"", request.getUrl().queryParameter("predicate"));

        // Assert: Response
        assertNotNull(places);
        assertEquals(2, places.size());

        // Assert: Response: First
        Place first = places.getFirst();
        assertEquals(ObjectType.PLACE, first.object);
        assertEquals(Long.valueOf(4442), first.id);
        assertEquals("Izz Cafe", first.name);
        assertEquals(Double.valueOf(51.8952597), first.location.lat);
        assertEquals(Double.valueOf(-8.4715779), first.location.lon);
        assertEquals(Long.valueOf(0), first.ratingStats.count);
        assertNull(first.ratingStats.likeRatio);
        assertEquals(Double.valueOf(0.5), first.ratingStats.score);
        assertEquals(OsmType.NODE, first.osmType);
        assertEquals(Long.valueOf(7095470096L), first.osmId);
        assertEquals("Cork", first.osmTags.get("addr:city"));
        assertEquals("restaurant", first.osmTags.get("amenity"));
        assertEquals("yes", first.osmTags.get("wheelchair"));
        assertEquals(14, first.osmTags.size());
    }

    @Test
    public void listReviewsOfPlace() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(200).body("""
                {
                  "object": "list",
                  "data": [
                   {
                      "comment": "Lovely little restaurant!",
                      "created": 0,
                      "id": 1,
                      "liked": true,
                      "n_likes": 3,
                      "object": "review",
                      "place": {
                        "id": 4442
                      },
                      "user": {
                        "display_name": "john",
                        "id": 7,
                        "object": "user"
                      }
                   },
                   {
                      "comment": "I didn't like the service much.",
                      "created": 0,
                      "id": 5,
                      "liked": false,
                      "n_likes": 2,
                      "object": "review",
                      "place": {
                        "id": 4442
                      },
                      "user": {
                        "display_name": "jane",
                        "id": 9,
                        "object": "user"
                      }
                    }
                  ]
                }
                """).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        List<Review> reviews = socialMaps.listReviewsOfPlace(4442L);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/places/4442/reviews", request.getUrl().encodedPath());

        // Assert: Response
        assertNotNull(reviews);
        assertEquals(2, reviews.size());

        // Assert: Response: First
        Review first = reviews.getFirst();
        assertEquals(ObjectType.REVIEW, first.object);
        assertEquals(Long.valueOf(1), first.id);
        assertEquals("Lovely little restaurant!", first.comment);
        assertEquals(Long.valueOf(7), first.userRef.id);
        User user = first.userRef.get();
        assertNotNull(user);
        assertEquals("john", user.displayName);
        assertEquals(Long.valueOf(4442), first.placeRef.id);
        Place place = first.placeRef.get();
        assertNull(place);
    }

    @Test
    public void createReview() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(202).body("""
                {
                  "comment": "Lovely little restaurant!",
                  "created": 0,
                  "id": 1,
                  "liked": true,
                  "n_likes": 3,
                  "object": "review",
                  "place": {
                    "id": 4442
                  },
                  "user": {
                    "id": 7
                  }
                }
                """).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        Review review = socialMaps.createReview("test-token", 4442L, "Lovely little restaurant!", true);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/v1/places/4442/reviews", request.getUrl().encodedPath());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));

        // Assert: Response
        assertNotNull(review);
        assertEquals(ObjectType.REVIEW, review.object);
        assertEquals(Long.valueOf(1L), review.id);
        assertEquals(true, review.liked);
        assertEquals("Lovely little restaurant!", review.comment);
        assertEquals(Long.valueOf(7L), review.userRef.id);
        assertNull(review.userRef.get());
        assertEquals(Long.valueOf(4442L), review.placeRef.id);
        assertNull(review.placeRef.get());
    }

    @Test
    public void deleteReview() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(204).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        socialMaps.deleteReview("test-token", 1L);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("DELETE", request.getMethod());
        assertEquals("/v1/reviews/1", request.getUrl().encodedPath());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void updateReview() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(202).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        socialMaps.updateReview("test-token", 1L, "Awful little restaurant!", false);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/reviews/1", request.getUrl().encodedPath());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void likeReview() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(204).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        socialMaps.likeReview("test-token", 1L);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/reviews/1/like", request.getUrl().encodedPath());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void unlikeReview() throws JSONException, IOException, InterruptedException {
        // Arrange
        serverRule.getServer().enqueue(new MockResponse.Builder().code(204).build());

        // Act
        SocialMaps socialMaps = new SocialMaps("test", baseUrl());
        socialMaps.unlikeReview("test-token", 1L);

        // Assert
        // Assert: Request
        RecordedRequest request = serverRule.getServer().takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("PUT", request.getMethod());
        assertEquals("/v1/reviews/1/unlike", request.getUrl().encodedPath());
        assertEquals("Bearer test-token", request.getHeaders().get("Authorization"));
    }

    private String baseUrl() {
        String url = serverRule.getServer().url("/").toString();
        return url.substring(0, url.length() - 1);
    }
}
