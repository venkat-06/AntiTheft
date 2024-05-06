package com.example.antitheft

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMail(
    private val context: Context,
    private val email: String,
    private val subject: String,
    private val message: String
) : AsyncTask<Void, Void, Void>() {

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.show(context, "Sending Pin to $email", "Please wait...", false, false)
    }

    private val session: Session by lazy {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "587")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "587")
        }

        Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("user.ashu6@gmail.com", "igpvnstxikvqfula")
            }
        })
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.show()
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        progressDialog.dismiss()
        Toast.makeText(context, "Pin Sent", Toast.LENGTH_LONG).show()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        try {
            val mm = MimeMessage(session)
            mm.setFrom(InternetAddress("user.ashu6@gmail.com"))
            mm.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            mm.subject = subject
            mm.setText(message)
            Transport.send(mm)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return null
    }
}


