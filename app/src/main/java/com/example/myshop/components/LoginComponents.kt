package com.example.myshop.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState : MutableState<String>,
               labelId: String = "Email",
               enabled :Boolean = true,
               imeAction: ImeAction = ImeAction.Next,
               onAction : KeyboardActions = KeyboardActions.Default){
    InputField(modifier = modifier, valueState = emailState, labelId = labelId, enabled = enabled, keyboardType = KeyboardType.Email, imeAction = imeAction, onAction = onAction)
}

@Composable
fun PasswordVisibilty(passwordVisibility: MutableState<Boolean>) {
    val visibility = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visibility }, ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = null)
    }
}
@Composable
fun PasswordInput(modifier: Modifier = Modifier,
                  passwordState : MutableState<String>,
                  labelId: String = "Password",
                  enabled :Boolean = true, passwordVisibility : MutableState<Boolean>,
                  imeAction: ImeAction = ImeAction.Next,
                  onAction : KeyboardActions = KeyboardActions.Default){
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value, onValueChange = { passwordState.value = it },
        modifier = modifier.fillMaxWidth().padding(20.dp),
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibilty(passwordVisibility=passwordVisibility)},
        keyboardActions = onAction
    )
}
@Composable
fun InputField(modifier: Modifier = Modifier,
               valueState : MutableState<String>,
               labelId: String,
               enabled :Boolean,
               isSingleLine:Boolean =true,
               keyboardType: KeyboardType = KeyboardType.Text,
               imeAction: ImeAction = ImeAction.Next,
               onAction : KeyboardActions = KeyboardActions.Default){
    OutlinedTextField(
        value = valueState.value, onValueChange = { valueState.value = it },
        modifier = modifier.fillMaxWidth().padding(20.dp),
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}

@Composable
fun ShowButton(textId:String,loading:Boolean,validInputs:Boolean,onClick :()-> Unit){
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth().padding(20.dp), enabled = !loading&&validInputs, shape = CircleShape) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}