package com.example.app_e_ligas;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class barangay_eventsActivity extends DrawerBasedActivity {

    ActivityBarangayEventsBinding activityBarangayEventsBinding;
    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    // Original list of events
    private List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayEventsBinding = ActivityBarangayEventsBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayEventsBinding.getRoot());
        allocateActivityTitle("Barangay Events");

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Sample events data
        events = generateSampleEvents();

        // Set up RecyclerView adapter with the original list of events
        eventAdapter = new EventAdapter(events);
        recyclerView.setAdapter(eventAdapter);

        // Set up CalendarView listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Filter events based on selected date
            List<Event> filteredEvents = filterEventsByDate(year, month, dayOfMonth);
            // Update RecyclerView adapter with filtered events
            eventAdapter.setEvents(filteredEvents);

            // Highlight selected day's events
            highlightSelectedDay(year, month, dayOfMonth);
        });

        // Set up RecyclerView item click listener
        eventAdapter.setOnItemClickListener((view, event) -> {
            // Handle item click, e.g., show event description
            Toast.makeText(barangay_eventsActivity.this, "Clicked on " + event.getEventName(), Toast.LENGTH_SHORT).show();
            // You can access other properties of the event, e.g., event.getEventDescription()
        });

    }

    // Method to generate sample events
    private List<Event> generateSampleEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1", 2023, 1, 1));
        events.add(new Event("Event 2", 2023, 1, 15));
        events.add(new Event("Event 3", 2023, 2, 10));
        // Add more events as needed
        return events;
    }

    // Method to filter events based on selected date
    private List<Event> filterEventsByDate(int year, int month, int dayOfMonth) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getYear() == year && event.getMonth() == month && event.getDayOfMonth() == dayOfMonth) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    // Method to highlight selected day's events
    private void highlightSelectedDay(int year, int month, int dayOfMonth) {
        // TODO: Implement highlighting logic, e.g., change background color of the selected day
        // You may use a library or custom logic to achieve this based on your UI design.
    }
}
