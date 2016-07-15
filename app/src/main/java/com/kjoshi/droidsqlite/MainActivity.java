package com.kjoshi.droidsqlite;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kjoshi.droidsqlite.controller.CustomerController;
import com.kjoshi.droidsqlite.model.Customer;

/**
 * The MainActivity class will be launcher class for this application where it will ada customer in SQLite database.
 *
 * @author Khemchand Joshi
 * @version 1.0
 * @since 2016-06-29
 */

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    //Define all input layouts
    TextInputLayout mFirstNameInputLayout, mLastNameInputLayout, mEmailInputLayout, mPhoneInputLayout, mCompanyInputLayout;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the toolbar on top
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //Initialize all the UI widgets
        initializeUIWidgets();
        //Initialize progress dialog
        mDialog = new ProgressDialog(this);
        //set on click listener fr when user click on "Add New Customer" button
        Button mAddCustomerButton = (Button) findViewById(R.id.addCustomerButton);
        mAddCustomerButton.setOnClickListener(AddCustomerButtonListener);

    }

    /**
     * This is the method to initialize all the UI widgets(InputTextLayout, EdiText, etc defined in activity_main.xml.
     *
     * @param No parameters used.
     * @return Nothing.
     * @throws IOException On input error.
     */

    private void initializeUIWidgets() {
        try {
            //Initialize all the InputTextLayout
            mFirstNameInputLayout = (TextInputLayout) findViewById(R.id.firstNameInputLayout);
            mLastNameInputLayout = (TextInputLayout) findViewById(R.id.lastNameInputLayout);
            mEmailInputLayout = (TextInputLayout) findViewById(R.id.emailInputLayout);
            mPhoneInputLayout = (TextInputLayout) findViewById(R.id.phoneInputLayout);
            mCompanyInputLayout = (TextInputLayout) findViewById(R.id.companyInputLayout);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * This is the anonymous class for OnClick interface implementor when user press add customer button.
     *
     */
    private View.OnClickListener AddCustomerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                //Extract all the data from EditText
                String firstName = mFirstNameInputLayout.getEditText().getText().toString();
                String lastName = mLastNameInputLayout.getEditText().getText().toString();
                String email = mEmailInputLayout.getEditText().getText().toString();
                String phone = mPhoneInputLayout.getEditText().getText().toString();
                String company = mCompanyInputLayout.getEditText().getText().toString();
                //Set a boolean to check if there is any error in input variable
                boolean isError = false;
                //Check whether any value is null or blank then show a error message.
                if (firstName.trim().isEmpty()) {
                    mFirstNameInputLayout.setError(getResources().getString(R.string.first_name_blank));
                    isError = true;
                } else {
                    mFirstNameInputLayout.setErrorEnabled(false);
                }
                if (lastName.trim().isEmpty()) {
                    mLastNameInputLayout.setError(getResources().getString(R.string.last_name_blank));
                    isError = true;
                } else {
                    mLastNameInputLayout.setErrorEnabled(false);
                }
                if (email.trim().isEmpty()) {
                    mEmailInputLayout.setError(getResources().getString(R.string.email_blank));
                    isError = true;
                } else {
                    mEmailInputLayout.setErrorEnabled(false);
                }
                if (phone.trim().isEmpty()) {
                    mPhoneInputLayout.setError(getResources().getString(R.string.phone_blank));
                    isError = true;
                } else {
                    mPhoneInputLayout.setErrorEnabled(false);
                }
                if (company.trim().isEmpty()) {
                    mCompanyInputLayout.setError(getResources().getString(R.string.company_blank));
                    isError = true;
                } else {
                    mCompanyInputLayout.setErrorEnabled(false);
                }
                //If there is no error then call asynchronous task to create a customer in database.
                if (!isError) {
                    //Call Customer constructor to create a Customer object.
                    Customer customer = new Customer(null, firstName, lastName, email, phone, company);
                    //Call asynchronous class to add a customer in database and return to home screen
                    new CustomerAsyncTask().execute(customer);

                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }
    };

    /**
     * This is the regular inner class which will save customer information in database asynchronously and after successfully save, will display a Toast message to user.
     *
     * @param Customer[] as signature it accepts only array as argument so customer object would be passed to save in database..
     * @return true or false if successfully saved in database then true otherwise false.
     * @throws IOException On input error.
     */
    private class CustomerAsyncTask extends AsyncTask<Customer, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Customer... customers) {
            try {
                CustomerController controller = new CustomerController(MainActivity.this);
                controller.addCustomer(customers[0]);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            try {
                mDialog.setMessage(getResources().getString(R.string.dialog_customer_save));
                mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDialog.setIndeterminate(true);
                mDialog.setProgress(0);
                mDialog.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
