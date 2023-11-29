package com.image.imagepixel

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import com.google.android.material.card.MaterialCardView
import com.image.imagepixel.databinding.ActivityPasswordVibrationBinding
import com.image.imagepixel.databinding.ActivityPasswordVibrationMainBinding

class PasswordVibrationMainActivity : AppCompatActivity() {

    private var binding: ActivityPasswordVibrationMainBinding?= null
    private val predefinedPassword = "123456"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordVibrationMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        handleOTPTypingEvent()
        //setSamplePassword()
    }
    private fun handleOTPTypingEvent() {
        binding?.etOTPTextOne?.apply {
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextOne, isDigitCorrect(0, text))
                if (text?.length == 1) {
                    binding?.etOTPTextTwo?.requestFocus()
                }
            }
        }
        binding?.etOTPTextTwo?.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL && text?.isEmpty() == true) {
                        binding?.etOTPTextOne?.requestFocus()
                        return true
                    }
                    return false
                }
            })
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextTwo, isDigitCorrect(1, text))
                if (text?.length == 1) {
                    binding?.etOTPTextThree?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextTwo, true)
                } else {
                    binding?.etOTPTextOne?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextTwo, false)
                }
            }
        }
        binding?.etOTPTextThree?.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL && text?.isEmpty() == true) {
                        binding?.etOTPTextTwo?.requestFocus()
                        return true
                    }
                    return false
                }
            })
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextThree, isDigitCorrect(2, text))
                if (text?.length == 1) {
                    binding?.etOTPTextFour?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextThree, true)
                } else {
                    binding?.etOTPTextTwo?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextThree, false)
                }
            }
        }
        binding?.etOTPTextFour?.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL && text?.isEmpty() == true) {
                        binding?.etOTPTextThree?.requestFocus()
                        return true
                    }
                    return false
                }
            })
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextFour, isDigitCorrect(3, text))
                if (text?.length == 1) {
                    binding?.etOTPTextFive?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextFour, true)
                } else {
                    binding?.etOTPTextThree?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextFour, false)
                }
            }
        }
        binding?.etOTPTextFive?.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL && text?.isEmpty() == true) {
                        binding?.etOTPTextFour?.requestFocus()
                        return true
                    }
                    return false
                }
            })
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextFive, isDigitCorrect(4, text))
                if (text?.length == 1) {
                    binding?.etOTPTextSix?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextFive, true)
                } else {
                    binding?.etOTPTextFour?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextFive, false)
                }
            }
        }
        binding?.etOTPTextSix?.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL && text?.isEmpty() == true) {
                        binding?.etOTPTextFive?.requestFocus()
                        return true
                    }
                    return false
                }
            })
            doOnTextChanged { text, _, _, _ ->
                handleUiForOTPText(binding?.cvOTPTextSix, isDigitCorrect(5, text))
                if (text?.length == 0) {
                    binding?.etOTPTextFive?.requestFocus()
                    handleUiForOTPText(binding?.cvOTPTextSix, false)
                } else {
                    handleUiForOTPText(binding?.cvOTPTextSix, true)
                }
            }
        }
    }

    private fun isDigitCorrect(position: Int, enteredDigit: CharSequence?): Boolean {
        val correctDigit = predefinedPassword.getOrNull(position)
        return enteredDigit != null && enteredDigit.length == 1 && enteredDigit[0] == correctDigit
    }
    private fun handleUiForOTPText(cardView: MaterialCardView?, active: Boolean) {
        if (active) {
            cardView?.setCardBackgroundColor(Color.WHITE)
            cardView?.strokeColor = resources.getColor(R.color.colorPrimary)
        } else {
            cardView?.setCardBackgroundColor(Color.WHITE)
            cardView?.strokeColor = Color.RED  // Set a different color for incorrect input
            // Vibrate for incorrect input
            vibrateOnIncorrectInput()
        }
    }

    private fun vibrateOnIncorrectInput() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(100)
        }
    }

    private fun setSamplePassword() {
        binding?.cslVerify?.setOnClickListener {
            // Get the entered OTP values
            val otp = getEnteredOTP()
            // Check if the entered OTP matches the sample password
            if (otp == predefinedPassword) {
                // Password is correct, you can perform the desired action here
                // For testing purposes, let's vibrate the device
                vibrateOnCorrectPassword()
            } else {
                // Password is incorrect, vibrate the device to indicate a wrong password
                vibrateOnCorrectPassword()
            }
        }
    }

    private fun getEnteredOTP(): String {
        // Concatenate the entered OTP values
        return StringBuilder().apply {
            append(binding?.etOTPTextOne?.text)
            append(binding?.etOTPTextTwo?.text)
            append(binding?.etOTPTextThree?.text)
            append(binding?.etOTPTextFour?.text)
            append(binding?.etOTPTextFive?.text)
            append(binding?.etOTPTextSix?.text)
        }.toString()
    }

    // Function to vibrate the device for a correct password (for testing purposes)
    private fun vibrateOnCorrectPassword() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(200)
        }
    }
}