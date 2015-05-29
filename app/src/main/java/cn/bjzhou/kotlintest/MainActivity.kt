package cn.bjzhou.kotlintest

import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.text.InputType
import android.widget.Toast
import org.jetbrains.anko.*


public class MainActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            editText {
                hint = "username"
            }
            editText {
                hint = "password"
                inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            button {
                text = "submit"
                onClick {
                    Toast.makeText(this@MainActivity, "on submit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
