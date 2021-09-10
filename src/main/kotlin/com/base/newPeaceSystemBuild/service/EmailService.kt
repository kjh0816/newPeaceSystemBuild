package com.base.newPeaceSystemBuild.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailSender: JavaMailSender) {

    private val notifyEmailFrom: String = "no-reply@no-reply.com"

    fun send(to: String, subject: String, body: String) {

        val message = emailSender.createMimeMessage()

        val helper = MimeMessageHelper(message)

        helper.setFrom(notifyEmailFrom)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(body, true)

        emailSender.send(message)
    }
}