package com.example.cityquest.adapter;

// CityArrayAdapter.java
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

public class CityArrayAdapter extends ArrayAdapter<String> {
    private List<String> originalSuggestions;
    private List<String> filteredSuggestions;
    private CityFilter filter;

    public CityArrayAdapter(Context context, List<String> suggestions) {
        super(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        this.originalSuggestions = new ArrayList<>(suggestions);
        this.filteredSuggestions = new ArrayList<>(suggestions);
        filter = new CityFilter();
    }

    @Override
    public int getCount() {
        return filteredSuggestions.size();
    }

    @Override
    public String getItem(int position) {
        return filteredSuggestions.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CityFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(originalSuggestions);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String suggestion : originalSuggestions) {
                    if (suggestion.toLowerCase().contains(filterPattern)) {
                        suggestions.add(suggestion);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredSuggestions.clear();
            filteredSuggestions.addAll((List) results.values);
            notifyDataSetChanged();
        }
    }
}
