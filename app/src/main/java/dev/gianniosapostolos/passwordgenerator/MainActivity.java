package dev.gianniosapostolos.passwordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    // We need to be able to hide and show the GridLayout that contains the buttons necessary for
    // creating a custom password
    GridLayout customOptionsGrid;

    EditText excludedCharactersEditText;
    EditText passwordLengthEditText;
    EditText passwordEditText;



    Spinner spinner;


    CheckBox digitsCheckBox;
    CheckBox allLettersCheckBox;
    CheckBox lowercaseLettersCheckBox;
    CheckBox uppercaseLettersCheckBox;
    CheckBox symbolsCheckBox;

    boolean isCustomPassword;
    int passwordLength=15;

    StringBuilder passwordCharacters;
    Set<String> excludedCharacters;

    Toast toast; // This is used in a function called ShowToastMessage()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.customOptionsGrid = (GridLayout) findViewById(R.id.customOptionsGridLayout);
        this.excludedCharactersEditText = (EditText) findViewById(R.id.excludedCharactersEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordResult);
        this.passwordLengthEditText = (EditText) findViewById(R.id.passwordLengthEditText);


        this.digitsCheckBox = (CheckBox) findViewById(R.id.checkbox_digits);
        this.allLettersCheckBox = (CheckBox) findViewById(R.id.checkbox_allChars);
        this.lowercaseLettersCheckBox = (CheckBox) findViewById(R.id.checkbox_smallChars);
        this.uppercaseLettersCheckBox = (CheckBox) findViewById(R.id.checkbox_capitalChars);
        this.symbolsCheckBox = (CheckBox) findViewById(R.id.checkbox_symbols);

        this.passwordCharacters = new StringBuilder();
        this.excludedCharacters = new HashSet<>();

        this.isCustomPassword = false;


        //populate the spinner (dropdown) with the options located at res/values/strings.xml -> passwordOptions_array
        this.spinner = (Spinner) findViewById(R.id.optionsDropdown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.passwordOptions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();

        if (!text.equals(getResources().getString(R.string.customStringSpinnerItem))) {
            //Toast.makeText(parent.getContext(), "Password set to: " + text, Toast.LENGTH_SHORT).show();
            this.customOptionsGrid.setVisibility(View.GONE);
            this.isCustomPassword = false;

            if(text.equals(getResources().getString(R.string.allSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*-+(){}/|<>?_=");
            }
            else if(text.equals(getResources().getString(R.string.alphanumericalSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
            }
            else if(text.equals(getResources().getString(R.string.digitsSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("1234567890");
            }
            else if(text.equals(getResources().getString(R.string.allLettersSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
            }
            else if(text.equals(getResources().getString(R.string.lowercaseSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("abcdefghijklmnopqrstuvwxyz");
            }
            else if(text.equals(getResources().getString(R.string.uppercaseSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            }
            else if(text.equals(getResources().getString(R.string.symbolsSpinnerItem)))
            {
                passwordCharacters.setLength(0);
                passwordCharacters.trimToSize();
                passwordCharacters.append("!@#$%^&*-+(){}/|<>?_=");
            }



            //Toast.makeText(getApplicationContext(), "Characters " + passwordCharacters, Toast.LENGTH_SHORT).show();

        } else {
            this.customOptionsGrid.setVisibility(View.VISIBLE);
            isCustomPassword = true;
            if(!lowercaseLettersCheckBox.isChecked() && !uppercaseLettersCheckBox.isChecked()) // Prevent having the All letters checkbox checked if either the upper or lower case option is checked
                allLettersCheckBox.setChecked(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinner.setSelection(0); //default selection -> First spinner element
        spinner.setOnItemSelectedListener(this);
    }

    public void onCheckboxClicked(View view) {
        //Toast.makeText(getApplicationContext(), getResources().getResourceEntryName(view.getId()), Toast.LENGTH_SHORT).show(); //displaying the id of a checkbox.

        isCustomPassword=true;

        String  checkboxID = getResources().getResourceEntryName(view.getId());

        if(checkboxID.equals("checkbox_allChars"))
        {
            if(allLettersCheckBox.isChecked())
            {
                uppercaseLettersCheckBox.setChecked(false);
                lowercaseLettersCheckBox.setChecked(false);
            }
        }

        if(checkboxID.equals("checkbox_capitalChars") || checkboxID.equals("checkbox_smallChars"))
        {
            allLettersCheckBox.setChecked(false);
        }

    }


    // Copies the password from the passwordEditText and saves it to clipboard
    public void onCopyPasswordClicked(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied password", passwordEditText.getText().toString());
        clipboard.setPrimaryClip(clip);


        ShowToastMessage("Password copied",0);
    }

    public void onGeneratePasswordClicked(View view) {


        excludedCharacters = EditExcludedCharacters(excludedCharactersEditText);

        if (isCustomPassword) {
            passwordCharacters.setLength(0);
            passwordCharacters.trimToSize();

            if (digitsCheckBox.isChecked())
                passwordCharacters.append("1234567890");

            if (allLettersCheckBox.isChecked() && (!uppercaseLettersCheckBox.isChecked() && !lowercaseLettersCheckBox.isChecked()))
                passwordCharacters.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

            if (uppercaseLettersCheckBox.isChecked())
                passwordCharacters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

            if (lowercaseLettersCheckBox.isChecked())
                passwordCharacters.append("abcdefghijklmnopqrstuvwxyz");

            if (symbolsCheckBox.isChecked())
                passwordCharacters.append("!@#$%^&*-+(){}/|<>?_=");

        }



        if (IsPasswordAvailableForGeneration(excludedCharacters, passwordCharacters))
        {

            try
            {
                passwordLength= Integer.parseInt(passwordLengthEditText.getText().toString());
                if(passwordLength<=0)
                    passwordLength=15;
            }
            catch(NumberFormatException e)
            {
                passwordEditText.setText("Unsupported Length value.");
                passwordLength=15;
            }


            PasswordGenerator passwordGenerator = new PasswordGenerator(passwordLength, passwordCharacters.toString(), excludedCharacters);
            passwordEditText.setText(passwordGenerator.GeneratePassword());

            // This line checks  if an edit text is empty.
            // It gets its text as string and trims it (removes whitespaces from both ends of a string) and then checks the length.
            // If it's 0 it means that the EditText is empty
            if(passwordLengthEditText.getText().toString().trim().length()==0) {
                ShowToastMessage("Password length is left empty. Consider adjusting it\nA default length of 15 has been chosen", 1);
                passwordLengthEditText.setText("15");
            }

        }
        else
        {
            passwordEditText.setText("INVALID PASSWORD PARAMETERS");
                ShowToastMessage("Cannot generate a password where all available characters are excluded!",1);
        }
    }

    //Create a Set of characters without spaces and commas from an EditText field.
    private static Set<String> EditExcludedCharacters(EditText excludedCharactersEditText) {
        Set<String> excludedCharacters = new HashSet<>();
        String userInput = excludedCharactersEditText.getText().toString();


        for (int i = 0; i < userInput.length(); i++) {
            char characterRead = userInput.charAt(i);
            if (characterRead != ',' && characterRead != ' ') {
                excludedCharacters.add(Character.toString(characterRead));
            }
        }

        return excludedCharacters;
    }


    // Check if a password can be generated based on the excluded characters
    // Returns true if a password can be generated.
    private boolean IsPasswordAvailableForGeneration(Set<String> excludedCharacters, StringBuilder passwordCharacters)
    {
        for (int i = 0; i < passwordCharacters.length(); i++)
            if (!excludedCharacters.contains(Character.toString(passwordCharacters.charAt(i))))
                return true; // If even one of the passwordCharacters CANNOT be found in the excluded set then the password can be generated.

        return false; // If all passwordCharacters are excluded then a password cannot be generated. So we return false.
    }

    // This function is responsible for creating a toast message with a given input.
    // It is used because the user might press an action that creates a Toast multiple times and it might take some time
    // for the screen to clear off all toasts. This function stops the previous toasts (if there's one) and then shows the new one
    // The duration parameter is responsible for the toast length. 1= LENGTH_LONG 0 (or anything else) = LENGTH_SHORT
    public void ShowToastMessage(String messageToShow, int duration)
    {
        if(toast !=null)
            toast.cancel();

        if(duration==1)
            toast=Toast.makeText(getApplicationContext(),messageToShow, Toast.LENGTH_LONG);
        else
            toast=Toast.makeText(getApplicationContext(),messageToShow, Toast.LENGTH_SHORT);

        toast.show();
    }


}
