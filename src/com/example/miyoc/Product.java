package com.example.miyoc;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Product extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// storing string resources into Array
		String[] adobe_products = getResources().getStringArray(
				R.array.adobe_products);

		// Binding resources Array to ListAdapter
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, adobe_products));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			startActivity(new Intent(this, Product_Tops.class));
			break;
		case 1:
			startActivity(new Intent(this, Product_Bottoms.class));
			break;
		case 2:
			startActivity(new Intent(this, Product_Dresses.class));
			break;
		case 3:
			startClothInfoActivity("f"); //Featured Collection.
			break;
		case 4:
			startClothInfoActivity("w"); // Women
			break;
		case 5:
			startClothInfoActivity("m"); // Men
			break;
		case 6:
			startClothInfoActivity("a"); //accessories
			break;
		case 7:
			startClothInfoActivity("b"); // Brands.
			break;
		}

	}
	
	private void startClothInfoActivity(String mode) {
		Intent intent = new Intent(this, ClothInfoActivity.class);
		intent.putExtra("mode", mode); // Brands.
		startActivity(intent);
	}

	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
}