package com.idl.daq;

import java.util.ArrayList;
import java.util.List;

import com.daq.sensors.I2CProc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class I2C_ExecFragment extends Fragment implements OnClickListener {

	View rootView;
	ListView lv;
	FButton read, write, delay;
	List<I2C_ItemClass> list;
	Context context;
	I2C_Adapter adapter;
	I2C_ItemClass obj;
	String TAG;
	
	GlobalState gS;
	I2CProc tempSensor;

	Callbacks i2c_exec_callback;
	public interface Callbacks {

		public void makeToast(String t);

		public Context getContext();

		public void openFormula(String string);

		public void openConfig();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub

		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		setRetainInstance(true);
		L.d("i2c config fragment attached!");
		i2c_exec_callback = (Callbacks) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.list_layout_i2c_exec, container, false);
		defineAttributes();
		setHasOptionsMenu(true);
		return rootView;
	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		context = getActivity();
		gS = (GlobalState) i2c_exec_callback.getContext();
		tempSensor = (I2CProc) gS.getSensor();
		lv = (ListView) rootView.findViewById(R.id.listView_i2c_exec);
		// context = this;
		read = (FButton) rootView.findViewById(R.id.i2c_read_exec);
		write = (FButton) rootView.findViewById(R.id.i2c_write_exec);
		delay = (FButton) rootView.findViewById(R.id.i2c_delay_exec);
		list = new ArrayList<I2C_ItemClass>();
		adapter = new I2C_Adapter(context, list,R.layout.list_layout_i2c_exec);
		lv.setAdapter(adapter);

		obj = new I2C_ItemClass();
		read.setOnClickListener(this);
		write.setOnClickListener(this);
		delay.setOnClickListener(this);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.i2c_exec_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		tempSensor.setExecList(list);
		switch(item.getItemId()){
		case R.id.formula_menu:
			i2c_exec_callback.openFormula("");
			break;
		case R.id.config_menu:
			i2c_exec_callback.openConfig();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.i2c_read_exec:
			obj = new I2C_ItemClass();
			obj.setType("read");
			list.add(obj);
			readInstruction(list.size() - 1);
			break;
		case R.id.i2c_write_exec:
			obj = new I2C_ItemClass();
			obj.setType("write");
			list.add(obj);
			writeInstruction(list.size() - 1);
			break;
		case R.id.i2c_delay_exec:
			obj = new I2C_ItemClass();
			obj.setType("delay");
			list.add(obj);
			delayInstruction(list.size() - 1);
			break;
		}

		adapter.notifyDataSetChanged();
		lv.setSelection(list.size() - 1);
	}

	private void delayInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_view, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.editText1);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setDelay(
										addr.getText().toString());
								adapter.notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void writeInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_view, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.editText1);
		final EditText val = (EditText) dialogview.findViewById(R.id.editText2);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setAddr(
										addr.getText().toString());
								list.get(position).setVal(
										val.getText().toString());
								adapter.notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void readInstruction(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(context);
		View dialogview = li.inflate(R.layout.i2c_dialog_view, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogview);
		final EditText addr = (EditText) dialogview
				.findViewById(R.id.editText1);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Commit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								list.get(position).setAddr(
										addr.getText().toString());
								adapter.notifyDataSetChanged();
								dialog.dismiss();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
