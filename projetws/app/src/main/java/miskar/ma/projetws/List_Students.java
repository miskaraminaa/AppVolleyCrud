package miskar.ma.projetws;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import miskar.ma.projetws.adapter.EtudiantAdapter;
import miskar.ma.projetws.beans.Etudiant;

public class List_Students extends AppCompatActivity {
    private static final String TAG = "ListEtudiantsActivity";
    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private List<Etudiant> etudiantList;
    private RequestQueue requestQueue;
    private static final String LOAD_URL = "http://192.168.1.20/PhpProject1/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the up button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etudiantList = new ArrayList<>();
        adapter = new EtudiantAdapter(this, etudiantList);
        recyclerView.setAdapter(adapter);
        adapter.attachSwipeToDelete(recyclerView);

        requestQueue = Volley.newRequestQueue(this);
        loadEtudiants();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadEtudiants() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            // Parse the JSON array
                            Type listType = new TypeToken<List<Etudiant>>() {}.getType();
                            List<Etudiant> loadedEtudiants = new Gson().fromJson(response, listType);

                            if (loadedEtudiants == null || loadedEtudiants.isEmpty()) {
                                throw new Exception("Parsed student list is null or empty");
                            }

                            etudiantList.clear();
                            etudiantList.addAll(loadedEtudiants);
                            adapter.updateData(loadedEtudiants);
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            Log.e(TAG, "JSON syntax error: " + e.getMessage(), e);
                            Toast.makeText(List_Students.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, "Error loading students", e);
                            Toast.makeText(List_Students.this, "Error loading students", Toast.LENGTH_SHORT).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage(), error);
                        Toast.makeText(List_Students.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }
}