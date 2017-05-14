package mobilebank.ru.mobilebank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import mobilebank.ru.mobilebank.Json;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Json JsonApi = new Json();
    TableLayout tableLayout;
    TextView    section;
    EditText    loginText;
    EditText    passwordText;
    Button      loginButton;
    Authorization AuthorizationInfo = new Authorization();
    TextView    authorizationTextInfo;
    TextView    authorizationStatus;
    Button      getAllInBank;
    EditText    branchId;
    Button      allInBranch;
    Button      allDetailed;
    Button      backAccounts;

    //tests
    JSONParser parser = new JSONParser();
    JSONObject chk = (JSONObject) parser.parse("{\n  \"error\" : \"false\",\n  \"message\" : \"Logged in!\",\n  \"session\" : \"A56ED89D19898076D20356AC17EF9553\"\n}");
    JSONObject getIndicativeRatesByTwoCurrencies = (JSONObject) parser.parse("{\n" +
            "  \"error\" : \"false\",\n" +
            "  \"message\" : \"Indicative rates list was retrieved\",\n" +
            "  \"result\" : [ {\n" +
            "    \"rateid\" : 1,\n" +
            "    \"branchid\" : 1,\n" +
            "    \"currency1\" : \"USD\",\n" +
            "    \"currency2\" : \"RUR\",\n" +
            "    \"indicativerate\" : 60.08\n" +
            "  }, {\n" +
            "    \"rateid\" : 3,\n" +
            "    \"branchid\" : 2,\n" +
            "    \"currency1\" : \"USD\",\n" +
            "    \"currency2\" : \"RUR\",\n" +
            "    \"indicativerate\" : 60.11\n" +
            "  }, {\n" +
            "    \"rateid\" : 2,\n" +
            "    \"branchid\" : 1,\n" +
            "    \"currency1\" : \"EUR\",\n" +
            "    \"currency2\" : \"RUR\",\n" +
            "    \"indicativerate\" : 65.00\n" +
            "  } ]\n" +
            "}");

    JSONObject getBalances = (JSONObject) parser.parse("{\n" +
            "  \"error\" : \"false\",\n" +
            "  \"message\" : \"Account list retrieved.\",\n" +
            "  \"result\" : [ {\n" +
            "    \"accountid\" : 1,\n" +
            "    \"branchid\" : 1,\n" +
            "    \"amount\" : 37658.67,\n" +
            "    \"currency\" : \"RUR\"\n" +
            "  }, {\n" +
            "    \"accountid\" : 2,\n" +
            "    \"branchid\" : 3,\n" +
            "    \"amount\" : 2500.0,\n" +
            "    \"currency\" : \"USD\"\n" +
            "  }, {\n" +
            "    \"accountid\" : 4,\n" +
            "    \"branchid\" : 4,\n" +
            "    \"amount\" : 53634.45,\n" +
            "    \"currency\" : \"RUR\"\n" +
            "  }, {\n" +
            "    \"accountid\" : 6,\n" +
            "    \"branchid\" : 3,\n" +
            "    \"amount\" : 6000.0,\n" +
            "    \"currency\" : \"USD\"\n" +
            "  } ]\n" +
            "}");

    JSONObject getBalanceSumInAllBranches = (JSONObject) parser.parse("{\n" +
            "  \"error\" : \"false\",\n" +
            "  \"message\" : \"Balance sum all branches retrieved.\",\n" +
            "  \"result\" : \"91293.12\"\n" +
            "}");

    JSONObject getBalanceSumInBranch = (JSONObject) parser.parse("{\n" +
            "  \"error\" : \"false\",\n" +
            "  \"message\" : \"Balance sum in branch was retrieved\",\n" +
            "  \"result\" : \"8500.00\"\n" +
            "}");

    public MainActivity() throws ParseException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        section = (TextView) findViewById(R.id.sectionId);
        authorizationTextInfo = (TextView) findViewById(R.id.authorizationText);
        authorizationStatus = (TextView) findViewById(R.id.authorization_status);
        loginText = (EditText) findViewById(R.id.login);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.LoginEnter);
        getAllInBank = (Button) findViewById(R.id.getAllInBank);
        branchId = (EditText) findViewById(R.id.branchId);
        allInBranch = (Button) findViewById(R.id.allInBranch);
        allDetailed = (Button) findViewById(R.id.AllDetailed);
        backAccounts = (Button) findViewById(R.id.backAccounts);
        loginText.setVisibility(View.INVISIBLE);
        passwordText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        getAllInBank.setVisibility(View.INVISIBLE);
        branchId.setVisibility(View.INVISIBLE);
        allInBranch.setVisibility(View.INVISIBLE);
        allDetailed.setVisibility(View.INVISIBLE);
        backAccounts.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //JSONObject res = null;
                JSONObject res = chk;
                //JsonApi.url = "/chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString();
                // получаем JSON строк с URL

                if(res.get("error").toString().equals("false"))
                {
                    AuthorizationInfo.setSessionKey(res.get("session").toString());
                    AuthorizationInfo.setUserName(loginText.getText().toString());
                    //authorizationTextInfo.setText("Вы авторизованы, как " + loginText.getText().toString());
                    authorizationStatus.setText("Вы авторизованы, как " + loginText.getText().toString());
                    authorizationStatus.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.INVISIBLE);
                    passwordText.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                }
                else
                {
                    authorizationStatus.setText("Ошибка авторизации " + res.get("message").toString());
                    authorizationStatus.setVisibility(View.VISIBLE);
                }

            }
        });

        getAllInBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONObject res = null;
                //String a = "http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString();
                JSONObject res = getBalanceSumInAllBranches;// Json.getJson("http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString());
                if(res.get("error").toString().equals("false"))
                {
                    //authorizationTextInfo.setText("Вы авторизованы, как " + loginText.getText().toString());
                    authorizationStatus.setText("На ваших счетах всего " + res.get("result").toString() + "рублей");
                    getAllInBank.setVisibility(View.INVISIBLE);
                    branchId.setVisibility(View.INVISIBLE);
                    allInBranch.setVisibility(View.INVISIBLE);
                    allDetailed.setVisibility(View.INVISIBLE);
                    backAccounts.setVisibility(View.VISIBLE);
                }
                else
                {
                    authorizationStatus.setText("Ошибка" + res.get("message").toString());
                }

            }
        });

        allInBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONObject res = null;
                //String a = "http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString();
                JSONObject res = getBalanceSumInBranch;// Json.getJson("http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString());
                if(res.get("error").toString().equals("false"))
                {
                    //authorizationTextInfo.setText("Вы авторизованы, как " + loginText.getText().toString());
                    authorizationStatus.setText("На ваших в филиале " + branchId.getText() + " счетах всего " + res.get("result").toString() + "рублей");
                    getAllInBank.setVisibility(View.INVISIBLE);
                    branchId.setVisibility(View.INVISIBLE);
                    allInBranch.setVisibility(View.INVISIBLE);
                    allDetailed.setVisibility(View.INVISIBLE);
                    backAccounts.setVisibility(View.VISIBLE);
                }
                else
                {
                    authorizationStatus.setText("Ошибка" + res.get("message").toString());
                }

            }
        });

        allDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONObject res = null;
                //String a = "http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString();
                JSONObject res = getBalances;// Json.getJson("http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString());
                if(res.get("error").toString().equals("false"))
                {
                    getAllInBank.setVisibility(View.INVISIBLE);
                    branchId.setVisibility(View.INVISIBLE);
                    allInBranch.setVisibility(View.INVISIBLE);
                    allDetailed.setVisibility(View.INVISIBLE);
                    printTable(res);
                    backAccounts.setVisibility(View.VISIBLE);
                }
                else
                {
                    authorizationStatus.setText("Ошибка" + res.get("message").toString());
                }

            }
        });

        backAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONObject res = null;
                //String a = "http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api/" + "chk?name=" + loginText.getText().toString() + "&pass=" + passwordText.getText().toString();
                section.setText("Состояние счетов");
                tableLayout.removeAllViews();
                getAllInBank.setVisibility(View.VISIBLE);
                branchId.setVisibility(View.VISIBLE);
                allInBranch.setVisibility(View.VISIBLE);
                allDetailed.setVisibility(View.VISIBLE);
                backAccounts.setVisibility(View.INVISIBLE);
                authorizationStatus.setText("Вы авторизованы, как " + AuthorizationInfo.getUserName());

            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.exchange_rate) {
            if(AuthorizationInfo.getSessionKey() != null) {
                section.setText("Курс Валют");
                JSONObject res = getIndicativeRatesByTwoCurrencies;
                JSONArray resList = (JSONArray) res.get("result");

                tableLayout.setMinimumHeight(100);
                tableLayout.setVisibility(View.VISIBLE);
                loginText.setVisibility(View.INVISIBLE);
                passwordText.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);

                TableRow tableHead = new TableRow(this);
                tableHead.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                TextView resonHead1 = new TextView(this);
                resonHead1.setText("Покупка  ");
                resonHead1.setTextColor(Color.rgb(0, 0, 0));
                resonHead1.setBackgroundColor(Color.rgb(255, 255, 255));

                TextView resonHead2 = new TextView(this);
                resonHead2.setText("За    ");
                resonHead2.setTextColor(Color.rgb(0, 0, 0));
                resonHead2.setBackgroundColor(Color.rgb(255, 255, 255));

                TextView resonHead3 = new TextView(this);
                resonHead3.setText("По курсу");
                resonHead3.setTextColor(Color.rgb(0, 0, 0));
                resonHead3.setBackgroundColor(Color.rgb(255, 255, 255));

                tableHead.addView(resonHead1, 0);
                tableHead.addView(resonHead2, 1);
                tableHead.addView(resonHead3, 2);
                tableLayout.addView(tableHead, 0);

                for (int i = 0; i < resList.size(); i++) {

                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));

                    TextView reson1 = new TextView(this);
                    JSONObject temp = (JSONObject) resList.get(i);
                    reson1.setText((String) temp.get("currency1"));
                    reson1.setTextColor(Color.rgb(0, 0, 0));
                    reson1.setBackgroundColor(Color.rgb(255, 255, 255));

                    TextView reson2 = new TextView(this);
                    reson2.setText((String) temp.get("currency2"));
                    reson2.setTextColor(Color.rgb(0, 0, 0));
                    reson2.setBackgroundColor(Color.rgb(255, 255, 255));

                    TextView reson3 = new TextView(this);
                    reson3.setText((String) temp.get("indicativerate").toString());
                    reson3.setTextColor(Color.rgb(0, 0, 0));
                    reson3.setBackgroundColor(Color.rgb(255, 255, 255));

                    tableRow.addView(reson1, 0);
                    tableRow.addView(reson2, 1);
                    tableRow.addView(reson3, 2);
                    tableLayout.addView(tableRow, i + 1);
                }
            }
            else
            {
                authorizationStatus.setText("Ошибка. Необходимо авторизоваться");
            }

        } else if (id == R.id.accounts) {
            if(AuthorizationInfo.getSessionKey() != null)
            {
                section.setText("Состояние счетов");
                tableLayout.removeAllViews();
                getAllInBank.setVisibility(View.VISIBLE);
                branchId.setVisibility(View.VISIBLE);
                allInBranch.setVisibility(View.VISIBLE);
                allDetailed.setVisibility(View.VISIBLE);
            }
            else
            {
                authorizationStatus.setText("Ошибка. Необходимо авторизоваться");
            }

        } else if (id == R.id.authorization) {
            tableLayout.setVisibility(View.INVISIBLE);
            loginText.setVisibility(View.VISIBLE);
            passwordText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            section.setText("Авторизация");
            tableLayout.removeAllViews();
            if(AuthorizationInfo.getUserName() != null)
            {
                authorizationStatus.setText("Вы авторизованы, как " + AuthorizationInfo.getUserName());
                authorizationStatus.setVisibility(View.VISIBLE);
                loginText.setVisibility(View.INVISIBLE);
                passwordText.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                tableLayout.setVisibility(View.INVISIBLE);
                loginText.setVisibility(View.VISIBLE);
                passwordText.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                section.setText("Авторизация");
                tableLayout.removeAllViews();
            }

        } else if (id == R.id.subscribe) {
            if(AuthorizationInfo.getSessionKey() != null)
            {
                section.setText("Управление подписками");
                tableLayout.removeAllViews();
            }
            else
            {
                authorizationStatus.setText("Ошибка. Необходимо авторизоваться");
            }

        } else if (id == R.id.contacts) {
            section.setText("Обратная связь. Контакты");
            tableLayout.removeAllViews();

        } else if (id == R.id.message) {
            section.setText("Отправить сообщение");
            tableLayout.removeAllViews();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void printTable(JSONObject res)
    {
        JSONArray resList = (JSONArray) res.get("result");

        tableLayout.setMinimumHeight(100);
        tableLayout.setVisibility(View.VISIBLE);

        TableRow tableHead = new TableRow(this);
        tableHead.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        TextView resonHead1 = new TextView(this);
        resonHead1.setText("№  ");
        resonHead1.setTextColor(Color.rgb(0, 0, 0));
        resonHead1.setBackgroundColor(Color.rgb(255, 255, 255));

        TextView resonHead2 = new TextView(this);
        resonHead2.setText("Филиал    ");
        resonHead2.setTextColor(Color.rgb(0, 0, 0));
        resonHead2.setBackgroundColor(Color.rgb(255, 255, 255));

        TextView resonHead3 = new TextView(this);
        resonHead3.setText("Состояние");
        resonHead3.setTextColor(Color.rgb(0, 0, 0));
        resonHead3.setBackgroundColor(Color.rgb(255, 255, 255));

        tableHead.addView(resonHead1, 0);
        tableHead.addView(resonHead2, 1);
        tableHead.addView(resonHead3, 2);
        tableLayout.addView(tableHead, 0);

        for (int i = 0; i < resList.size(); i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            TextView reson1 = new TextView(this);
            JSONObject temp = (JSONObject) resList.get(i);
            reson1.setText(temp.get("accountid").toString());
            reson1.setTextColor(Color.rgb(0, 0, 0));
            reson1.setBackgroundColor(Color.rgb(255, 255, 255));

            TextView reson2 = new TextView(this);
            reson2.setText(temp.get("branchid").toString());
            reson2.setTextColor(Color.rgb(0, 0, 0));
            reson2.setBackgroundColor(Color.rgb(255, 255, 255));

            TextView reson3 = new TextView(this);
            reson3.setText(temp.get("amount").toString() + " " + temp.get("currency").toString());
            reson3.setTextColor(Color.rgb(0, 0, 0));
            reson3.setBackgroundColor(Color.rgb(255, 255, 255));

            tableRow.addView(reson1, 0);
            tableRow.addView(reson2, 1);
            tableRow.addView(reson3, 2);
            tableLayout.addView(tableRow, i + 1);
        }
    }
}
