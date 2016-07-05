package br.com.dhyegofernando.githubsample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import br.com.dhyegofernando.githubsample.adapters.github.RepositoryAdapter;
import br.com.dhyegofernando.githubsample.models.github.User;

public class MainActivity extends AppCompatActivity {

    protected TextView nameText;

    protected TextView usernameText;

    protected ImageView avatarImage;

    protected ListView repositoriesList;

    protected ProgressDialog progress;

    protected RequestQueue requestQueue;

    protected Gson gson;

    protected RepositoryAdapter repositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (TextView)findViewById(R.id.nameText);
        usernameText = (TextView)findViewById(R.id.usernameText);
        avatarImage = (ImageView)findViewById(R.id.avatarImage);
        repositoriesList = (ListView)findViewById(R.id.repositoriesList);
        progress = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();
        repositoryAdapter = new RepositoryAdapter(this);

        repositoriesList.setAdapter(repositoryAdapter);

        progress.setTitle("Searching...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSearch:
                openSearch();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void openSearch() {
        findUser();
    }

    protected void findUser() {
        openUserFinderDialog();
    }

    protected void findUser(String username) {
        progress.show();

        StringBuilder urlBuilder = new StringBuilder("https://api.github.com/users/");
        urlBuilder.append(username);

        Request userRequest = new JsonObjectRequest(Request.Method.GET, urlBuilder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();
                final User user = gson.fromJson(response.toString(), User.class);
                renderUser(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hide();
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(userRequest);
    }

    protected void openUserFinderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText usernameText = new EditText(this);

        builder.setTitle("Search by username");
        builder.setView(usernameText);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                findUser(usernameText.getText().toString());
            }
        });

        builder.show();
    }

    protected void renderUser(User user) {
        nameText.setText(user.name);
        usernameText.setText("@" + user.login);
        renderUserAvatar(user);
        repositoryAdapter.setUser(user);
    }

    private void renderUserAvatar(User user) {
        Request avatarRequest = new ImageRequest(user.avatarUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                avatarImage.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, null);

        requestQueue.add(avatarRequest);
    }
}
