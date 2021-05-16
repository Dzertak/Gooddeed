package com.kravchenko.apps.gooddeed.screen.adapter.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragment;
import com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragmentDirections;
import com.kravchenko.apps.gooddeed.screen.profile.SubscriptionsCallback;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.kravchenko.apps.gooddeed.screen.profile.EditProfileFragment.EDIT_PROFILE_KEY;

public class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<Category> categories;
    private static final int SUBSCRIPTION_TYPE = 0;
    private static final int SUBSCRIPTION_ADD_TYPE = 1;
    private boolean isEditable;
    private SubscriptionsCallback subscriptionCallback;


    public SubscriptionAdapter(Context context,
                               boolean isEditable,
                               SubscriptionsCallback subscriptionCallback) {
        this.context = context;
        this.subscriptionCallback = subscriptionCallback;
        this.categories = new ArrayList<>();
        this.isEditable = isEditable;
        if (isEditable) {
            //it's for make last item for adding new category
            categories.add(new Category());
        }
    }

    public SubscriptionAdapter(Context context,  boolean isEditable) {
        this.context = context;
        this.categories = new ArrayList<>();
        this.isEditable = isEditable;
        if (isEditable) {
            //it's for make last item for adding new category
            categories.add(new Category());
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        if (isEditable) {
            //it's for make last item for adding new category
            categories.add(new Category());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == SUBSCRIPTION_TYPE) {
            return new SubscriptionAdapter.SubViewHolder(inflater.inflate(R.layout.item_subscription, parent, false));
        } else
            return new SubscriptionAdapter.SubAddViewHolder(inflater.inflate(R.layout.item_subscription_add, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == categories.size() - 1 && isEditable ? SUBSCRIPTION_ADD_TYPE : SUBSCRIPTION_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == SUBSCRIPTION_TYPE) {
            SubViewHolder viewHolderSub = (SubViewHolder) holder;
            viewHolderSub.textViewTitle.setText(Utils.getString(categories.get(position).getTitle()));
            if (!isEditable) {
                viewHolderSub.imageViewRemove.setVisibility(View.GONE);
            } else {
                viewHolderSub.imageViewRemove.setOnClickListener(t -> {
                    subscriptionCallback.removeSubscription(position);
                });
            }
        } else {
            SubAddViewHolder viewHolderSub = (SubAddViewHolder) holder;
            viewHolderSub.textViewAdd.setOnClickListener(t -> {
                        subscriptionCallback.addSubscription();
                    }

            );
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private static class SubViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final ImageView imageViewRemove;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_subscription_title);
            imageViewRemove = itemView.findViewById(R.id.image_view_subscription_remove);
        }
    }

    private static class SubAddViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAdd;
        private final ImageView imageViewRemove;

        public SubAddViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAdd = itemView.findViewById(R.id.text_view_subscription_add);
            imageViewRemove = itemView.findViewById(R.id.image_view_subscription_remove);
        }
    }
}
