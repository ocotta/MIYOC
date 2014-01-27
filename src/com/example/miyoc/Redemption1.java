package com.example.miyoc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.RedeemFunction;
import com.example.miyoc.vo.Redeem;

public class Redemption1 extends Activity {

	private ListView listView;
	private RedeemAdapter adapter;
	private ProgressDialog progressDialog; 
	private TextView noredemption;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redemption);
		 
		listView = (ListView) this.findViewById(R.id.listview);
		noredemption = (TextView)this.findViewById(R.id.noredemption);
		String uid = new SessionManager(this).getUserDetails().get("uid");
		new RetrieveRedeemTask(uid).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		String uid = new SessionManager(this).getUserDetails().get("uid");
//		new RetrieveRedeemTask(uid).execute();
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

	List<Redeem> redeems = new ArrayList<Redeem>();
	private class RetrieveRedeemTask extends
			AsyncTask<Void, Void, List<Redeem>> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Redemption1.this, "Loading...", "Please wait...", true, false);
		}

		private RedeemFunction service = new RedeemFunction();
		private String uid;

		public RetrieveRedeemTask(String uid) {
			this.uid = uid;
		}

		@Override
		protected List<Redeem> doInBackground(Void... arg0) {
			JSONObject object = service.getRedeemList(Integer.valueOf(uid));
			redeems.clear();
			try {
				JSONArray array = object.getJSONArray("results");
				if(array == null) {
					return redeems;
				}
				for (int i = 0; i < array.length(); i++) {
					Redeem r = new Redeem();
					JSONObject obj = (JSONObject) array.get(i);
					r.id = obj.getInt("id");
					r.points = obj.getInt("points");
					redeems.add(r);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return redeems;
		}

		@Override
		protected void onPostExecute(List<Redeem> result) {
			if(result == null || result.size() == 0) {
				noredemption.setVisibility(View.VISIBLE);
			} else {
				noredemption.setVisibility(View.GONE);
			}
			if (adapter == null) {
				adapter = new RedeemAdapter(Redemption1.this, result);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			if(progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

	private class RedeemTask extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Redemption1.this, "Process...", "Please wait...", true, false);
		}

		private RedeemFunction service = new RedeemFunction();
		
		String email;
		Integer uid;
		Integer points;
		String name;
		
		public RedeemTask(String email, Integer uid, Integer points, String name) {
			this.email = email;
			this.uid = uid;
			this.points = points;
			this.name = name;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			if(progressDialog != null) {
				progressDialog.dismiss();
			}
			try {
				if(result.getBoolean("success")) {
					Toast.makeText(Redemption1.this, "Redeem successfully", Toast.LENGTH_SHORT).show();
					String uid = new SessionManager(Redemption1.this).getUserDetails().get("uid");
					new RetrieveRedeemTask(uid).execute();
				} else {
					String msg = result.getString("msg");
					if(msg != null) {
						Toast.makeText(Redemption1.this, msg, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(Redemption1.this, "Redeem Failed, Please try again later.", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject obj = service.redeem(email, uid, points, name);
			return obj;
		}

	}

	private class ViewHolder {
		public ImageView imgView;
		public Button redeemBtn;
	}

	private class RedeemAdapter extends BaseAdapter {

		private List<Redeem> redeems;
		private Context ctx;
		private LayoutInflater inflater;
		private ViewHolder viewHolder;

		public RedeemAdapter() {
		}

		public RedeemAdapter(Context ctx, List<Redeem> redeems) {
			this.redeems = redeems;
			inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.ctx = ctx;
		}

		@Override
		public int getCount() {
			return redeems.size();
		}

		@Override
		public Object getItem(int position) {
			return redeems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return redeems.get(position).id;
		}

		private void setImage(Integer points, ViewHolder viewHolder) {

			switch (points) {
			case 100:
				viewHolder.imgView.setImageResource(R.drawable.redemption_100);
				break;
			case 200:
				viewHolder.imgView.setImageResource(R.drawable.redemption_200);
				break;
			case 300:
				viewHolder.imgView.setImageResource(R.drawable.redemption_300);
				break;
			case 500:
				viewHolder.imgView.setImageResource(R.drawable.redemption_500);
				break;
			case 1000:
				viewHolder.imgView.setImageResource(R.drawable.redemption_1000);
				break;
			case 1800:
				viewHolder.imgView.setImageResource(R.drawable.redemption_1800);
				break;
			case 2500:
				viewHolder.imgView.setImageResource(R.drawable.redemption_2500);
				break;
			case 8000:
				viewHolder.imgView.setImageResource(R.drawable.redemption_8000);
				break;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Redeem r = redeems.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.redemption_item, null);
				viewHolder = new ViewHolder();
				viewHolder.imgView = (ImageView) convertView
						.findViewById(R.id.imgView);
				viewHolder.redeemBtn = (Button) convertView
						.findViewById(R.id.redeemBtn);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			setImage(r.points, viewHolder);

			viewHolder.redeemBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String uid = new SessionManager(ctx).getUserDetails().get("uid");
					String email = new SessionManager(ctx).getUserDetails().get("email");
					String name = new SessionManager(ctx).getUserDetails().get("name");
					new RedeemTask(email, Integer.valueOf(uid), r.points, name).execute();
				}

			});

			return convertView;
		}

	}

}
