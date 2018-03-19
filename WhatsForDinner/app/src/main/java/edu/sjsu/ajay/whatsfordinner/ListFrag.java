package edu.sjsu.ajay.whatsfordinner;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.ajay.whatsfordinner.Entities.NewRecipe;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends ListFragment {

    List<NewRecipe> recipes = new ArrayList<>();
    List<String> recipeNames = new ArrayList<>();
    private RecipeSelectedListener recipeSelectedListener;

    public ListFrag() {
        // Required empty public constructor
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipes = GetSaveData.read(this.getActivity());
        if(recipes == null)
            recipes = new ArrayList<>();

        for(NewRecipe n : recipes){
            recipeNames.add(n.getRecipeName());
        }

        setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, recipeNames));

        //If the phone is in landscape mode --> i.e. layout_default is null, then click on the first item
        if(this.getActivity().findViewById(R.id.recipes_layout_portrait) == null){
            recipeSelectedListener.onRecipeSelected(0);
        }
    }

    public interface RecipeSelectedListener{
        public void onRecipeSelected(int index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            recipeSelectedListener = (RecipeSelectedListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " Must Implement The Interface Called - recipeSelectedListener.!");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        recipeSelectedListener.onRecipeSelected(position);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list, container, false);
//    }

}
