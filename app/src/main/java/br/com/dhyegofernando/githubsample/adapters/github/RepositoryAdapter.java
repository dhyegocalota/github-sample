package br.com.dhyegofernando.githubsample.adapters.github;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import br.com.dhyegofernando.githubsample.R;
import br.com.dhyegofernando.githubsample.models.github.Repository;
import br.com.dhyegofernando.githubsample.models.github.User;

public class RepositoryAdapter extends ArrayAdapter<Repository> {
    protected User user;

    protected RequestQueue requestQueue;

    protected Gson gson;

    public RepositoryAdapter(Context context) {
        super(context, R.layout.repository_item, R.id.nameText);

        this.requestQueue = Volley.newRequestQueue(context);
        this.gson = new Gson();
    }

    public void setUser(User user) {
        this.user = user;
        this.refreshRepositories();
    }

    public User getUser() {
        return this.user;
    }

    public void refreshRepositories() {
        clear();

        StringBuilder urlBuilder = new StringBuilder("https://api.github.com/users/");
        urlBuilder.append(this.user.login);
        urlBuilder.append("/repos");

        Request userRepositoriesRequest = new JsonArrayRequest(urlBuilder.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final Repository[] repositories = gson.fromJson(response.toString(), Repository[].class);
                addAll(repositories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(userRepositoriesRequest);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Repository repository = getItem(position);

        TextView nameText = (TextView)view.findViewById(R.id.nameText);
        TextView langText = (TextView)view.findViewById(R.id.langText);
        TextView descriptionText = (TextView)view.findViewById(R.id.descriptionText);
        TextView viewsText = (TextView)view.findViewById(R.id.viewsText);
        TextView issuesText = (TextView)view.findViewById(R.id.issuesText);
        TextView forksText = (TextView)view.findViewById(R.id.forksText);
        ImageView iconImage = (ImageView)view.findViewById(R.id.iconImage);

        nameText.setText(repository.name);
        langText.setText(repository.language);
        descriptionText.setText(repository.description);
        viewsText.setText(Integer.toString(repository.watchersCount));
        issuesText.setText(Integer.toString(repository.openIssuesCount));
        forksText.setText(Integer.toString(repository.forksCount));

        int iconImageResource = (repository.isFork) ? R.drawable.ic_fork : R.drawable.ic_code;
        iconImage.setImageResource(iconImageResource);

        return view;
    }
}
