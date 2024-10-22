package miskar.ma.projetws.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RadioGroup;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miskar.ma.projetws.R;
import miskar.ma.projetws.beans.Etudiant;


public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {



    private List<Etudiant> etudiantList;
    private List<Etudiant> etudiantListFull;
    private Context context;

    private RequestQueue requestQueue;
    private static final String DELETE_URL = "http://192.168.1.20/PhpProject1/ws/deleteEtudiant.php";
    private static final String UPDATE_URL = "http://192.168.1.20/PhpProject1/ws/updateEtudiant.php";


    public EtudiantAdapter(Activity context, List<Etudiant> etudiantList) {
        this.etudiantList = new ArrayList<>(etudiantList);
        this.etudiantListFull = new ArrayList<>(etudiantList);

        this.context = context;

        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new EtudiantViewHolder(view, context, etudiantList);

    }

    @Override
    public int getItemCount() {
        // Use the filtered list size for item count
        return etudiantList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiantList.get(position);
        holder.bind(etudiant, holder);
    }

    private Filter etudiantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Etudiant> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(etudiantListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Etudiant etudiant : etudiantListFull) {
                    if (etudiant.getNom().toLowerCase().startsWith(filterPattern) ||
                            etudiant.getPrenom().toLowerCase().startsWith(filterPattern) ||
                            etudiant.getVille().toLowerCase().startsWith(filterPattern) ||
                            etudiant.getSexe().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(etudiant);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            etudiantList.clear();
            etudiantList.addAll((List<Etudiant>) results.values);
            notifyDataSetChanged();
        }
    };


    public Filter getFilter() {
        return etudiantFilter;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Etudiant> newList) {
        this.etudiantListFull = new ArrayList<>(newList);
        this.etudiantList.clear();
        this.etudiantList.addAll(newList);
        notifyDataSetChanged();
    }


    public void attachSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Etudiant etudiantToDelete = etudiantList.get(position);
                    showDeleteConfirmationDialog(etudiantToDelete, position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Ajoutez ici des effets visuels lors du glissement, si vous le souhaitez
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showDeleteConfirmationDialog(Etudiant etudiant, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmer la suppression");
        builder.setMessage("Voulez-vous vraiment supprimer cet étudiant ?");
        builder.setPositiveButton("Oui", (dialog, which) -> {
            deleteEtudiant(etudiant);
            etudiantList.remove(position);
            etudiantListFull.remove(position);
            notifyItemRemoved(position);
        });
        builder.setNegativeButton("Non", (dialog, which) -> {
            notifyItemChanged(position);
        });
        builder.setOnCancelListener(dialog -> {
            notifyItemChanged(position);
        });
        builder.show();
    }

    private void deleteEtudiant(Etudiant etudiant) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteEtudiant", "Response: " + response);
                        // Assume the response returns a success message
                        Toast.makeText(context, "Etudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteEtudiant", "Volley error: " + error.getMessage(), error);
                        Toast.makeText(context, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(etudiant.getId())); // Assuming Etudiant has a getId() method
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateEtudiant(Etudiant etudiant) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateEtudiant", "Response: " + response);
                        // Assume the response returns a success message
                        Toast.makeText(context, "Etudiant modifier avec succès", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateEtudiant", "Volley error: " + error.getMessage(), error);
                        Toast.makeText(context, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(etudiant.getId()));
                params.put("nom", String.valueOf(etudiant.getNom()));
                params.put("prenom", String.valueOf(etudiant.getPrenom()));
                params.put("ville", String.valueOf(etudiant.getVille()));
                params.put("sexe", String.valueOf(etudiant.getSexe()));
                params.put("image", String.valueOf(etudiant.getImage()));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    class EtudiantViewHolder extends RecyclerView.ViewHolder {
        public TextView nomTextView, prenomTextView, villeTextView, sexeTextView;
        public ImageView deleteIcon, updateIcon, profileImageView;
        public Context context;
        public List<Etudiant> etudiantList;

        EtudiantViewHolder(View itemView, Context context, List<Etudiant> etudiantList) {
            super(itemView);
            this.context = context;
            this.etudiantList = etudiantList;

            // Initialize views
            nomTextView = itemView.findViewById(R.id.nomTextView);
            prenomTextView = itemView.findViewById(R.id.prenomTextView);
            villeTextView = itemView.findViewById(R.id.villeTextView);
            sexeTextView = itemView.findViewById(R.id.sexeTextView);

            profileImageView = itemView.findViewById(R.id.profileImageView);

            setupListeners();
        }

        // Method to bind data to views
        void bind(Etudiant etudiant, EtudiantViewHolder holder) {
            nomTextView.setText(etudiant.getFormattedNom());
            prenomTextView.setText(etudiant.getFormattedPrenom());
            villeTextView.setText(etudiant.getFormattedVille());
            sexeTextView.setText(etudiant.getFormattedSexe());

            // Load and set the image
            String base64Image = etudiant.getImage();
            Log.d("EtudiantAdapter", "Image data: " + base64Image);

            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Decode Base64 string to byte array
                    byte[] decodedBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);

                    // Convert byte array to Bitmap
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                    // Use Glide to load the decoded Bitmap
                    Glide.with(holder.itemView.getContext())
                            .load(decodedBitmap)
                            .into(holder.profileImageView);
                } catch (IllegalArgumentException e) {
                    Log.e("EtudiantAdapter", "Invalid Base64 image data", e);
                    holder.profileImageView.setImageResource(R.drawable.default_profile_image); // Default image if decoding fails
                }
            } else {
                holder.profileImageView.setImageResource(R.drawable.default_profile_image); // Default image if no Base64 string
            }
        }
        private void showDeleteConfirmationDialog(final Etudiant etudiant) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmer la suppression")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cet étudiant ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEtudiant(etudiant);
                            int position = etudiantList.indexOf(etudiant);
                            if (position != -1) {
                                etudiantList.remove(position);
                                deleteEtudiant(etudiant);
                                notifyItemRemoved(position);
                            }
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        private void setupListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View popup = LayoutInflater.from(context).inflate(R.layout.update_student, null, false);

                    // Find the input fields from the popup layout
                    final ImageView img = popup.findViewById(R.id.img);
                    final EditText nom = popup.findViewById(R.id.editNom);
                    final EditText prenom = popup.findViewById(R.id.editPrenom);
                    final Spinner villeSpinner = popup.findViewById(R.id.editVille);
                    final RadioGroup sexeRadioGroup = popup.findViewById(R.id.sexeRadioGroup);

                    // Set existing data into the popup fields
                    nom.setText(extractValue(nomTextView.getText().toString()));
                    prenom.setText(extractValue(prenomTextView.getText().toString()));

                    // Set the current selected value for 'ville'
                    String currentVille = extractValue(villeTextView.getText().toString());
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) villeSpinner.getAdapter();
                    int villePosition = adapter.getPosition(currentVille);
                    villeSpinner.setSelection(villePosition);

                    // Set the current 'sexe' selection
                    String currentSexe = extractValue(sexeTextView.getText().toString());
                    if (currentSexe.equals("Homme")) {
                        sexeRadioGroup.check(R.id.radioMale);
                    } else if (currentSexe.equals("Femme")) {
                        sexeRadioGroup.check(R.id.radioFemale);
                    }

                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Modifier les informations")
                            .setView(popup)
                            .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Retrieve updated values from the input fields
                                    String updatedNom = nom.getText().toString();
                                    String updatedPrenom = prenom.getText().toString();
                                    String updatedVille = villeSpinner.getSelectedItem().toString();

                                    int selectedSexeId = sexeRadioGroup.getCheckedRadioButtonId();
                                    String updatedSexe = selectedSexeId == R.id.radioMale ? "Homme" : "Femme";

                                    // Update the UI or the data list
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        Etudiant etudiant = etudiantList.get(position);
                                        etudiant.setNom(updatedNom);
                                        etudiant.setPrenom(updatedPrenom);
                                        etudiant.setVille(updatedVille);
                                        etudiant.setSexe(updatedSexe);

                                        // Call the update method to update the backend
                                        updateEtudiant(etudiant);

                                        // Update the TextViews with the new values
                                        nomTextView.setText("Nom : " + updatedNom);
                                        prenomTextView.setText("Prénom : " + updatedPrenom);
                                        villeTextView.setText("Ville : " + updatedVille);
                                        sexeTextView.setText("Sexe : " + updatedSexe);

                                        notifyItemChanged(position);
                                    }
                                }
                            })
                            .setNegativeButton("Annuler", null)
                            .show();
                }
            });

            // Handle long click for deletion
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Etudiant etudiant = etudiantList.get(position);
                        showDeleteConfirmationDialog(etudiant);
                    }
                    return true; // Return true to indicate the event was handled
                }
            });
        }

        // Helper method to extract the value from formatted text
        private String extractValue(String formattedText) {
            int colonIndex = formattedText.indexOf(':');
            if (colonIndex != -1 && colonIndex < formattedText.length() - 1) {
                return formattedText.substring(colonIndex + 1).trim();
            }
            return formattedText;
        }



    }
}