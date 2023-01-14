package json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RawCollectionsExample {
  static class Event {
    private String name;
    private String source;
    private Event(String name, String source) {
      this.name = name;
      this.source = source;
    }
    @Override
    public String toString() {
      return String.format("(name=%s, source=%s)", name, source);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void main(String[] args) {
    Gson gson = new Gson();
    List<Event> events = new ArrayList<>();
    events.add(new Event("GREETINGS", "guest"));
    events.add(new Event("GREETINGS", "host"));
    events.add(new Event("GREETINGS", "James"));
    System.out.println(events);
    String jsonString = gson.toJson(events);
    System.out.println("Using Gson.toJson() on an ArrayList: " + jsonString);

    TypeToken<List<Event>> collectionType = new TypeToken<List<Event>>(){};
    List<Event> resultList = gson.fromJson(jsonString, collectionType);
    System.out.println(resultList);
  }
}