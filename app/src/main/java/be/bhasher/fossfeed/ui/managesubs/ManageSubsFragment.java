package be.bhasher.fossfeed.ui.managesubs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.NewSubActivity;
import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.databinding.FragmentManagesubsBinding;
import be.bhasher.fossfeed.ui.home.FeedAdapter;
import be.bhasher.fossfeed.ui.home.FeedItem;

public class ManageSubsFragment extends Fragment {
    private FragmentManagesubsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManagesubsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button button = root.findViewById(R.id.managesubs_addbutton);

        button.setOnClickListener(v -> requireContext().startActivity(new Intent(getContext(), NewSubActivity.class)));

        new SubsAdapter.Init(root).execute();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setSubActionBarIcon(-1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}