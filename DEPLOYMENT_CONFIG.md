# 🚀 Deployment Configuration Guide

## Email Reminder Setup for Production

The application sends email reminders using Gmail SMTP. Follow these steps to configure it for deployment:

### Option 1: Heroku Deployment

Set environment variables in Heroku:

```bash
# Set Gmail credentials
heroku config:set MAIL_USERNAME=your-email@gmail.com
heroku config:set MAIL_PASSWORD=your-app-password
heroku config:set MAIL_HOST=smtp.gmail.com
heroku config:set MAIL_PORT=587

# Set server port (Heroku assigns dynamically, but you can configure)
heroku config:set SERVER_PORT=8081
```

### Option 2: AWS/DigitalOcean/Other Cloud Deployment

Set environment variables when deploying:

```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
```

### Option 3: Docker Deployment

Pass environment variables in docker-compose or docker run:

```yaml
environment:
  MAIL_USERNAME: your-email@gmail.com
  MAIL_PASSWORD: your-app-password
  MAIL_HOST: smtp.gmail.com
  MAIL_PORT: 587
```

### Gmail Configuration Steps

1. **Enable 2-Factor Authentication** on your Gmail account
   - Visit: https://myaccount.google.com/security

2. **Generate App Password**
   - Visit: https://myaccount.google.com/apppasswords
   - Select "Mail" and "Windows Computer" (or your platform)
   - Google will provide a 16-character password
   - Use this as `MAIL_PASSWORD`

3. **Example Gmail Setup**
   ```
   MAIL_USERNAME: your-email@gmail.com
   MAIL_PASSWORD: xxxx xxxx xxxx xxxx  (16-character app password)
   ```

### Verify Email Configuration

Check application logs for successful initialization:

```
✓ Firebase initialized successfully
✓ JavaMailSender bean created (if emails configured)
```

### Troubleshooting

**Error: "Email Config Missing"**
- Environment variables not set
- Check that `MAIL_USERNAME` and `MAIL_PASSWORD` are configured

**Error: Authentication failed**
- Wrong app password (not Gmail password)
- 2-Factor Authentication not enabled
- Gmail account blocking "Less secure apps" (use App Password instead)

**Error: TLS not supported**
- Ensure STARTTLS is enabled (default: true)
- Port should be 587, not 465 or 25

**Email sends locally but not in production**
- Environment variables not passed to production environment
- Check deployment platform's environment variable settings
- Verify `MAIL_HOST` and `MAIL_PORT` in production

### Firebase Configuration

Also ensure Firebase credentials are set:

```bash
# Export Firebase service account JSON
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/firebase-service-account.json

# Or set as environment variable (base64 encoded)
heroku config:set FIREBASE_CREDENTIALS_BASE64=$(base64 -i firebase-service-account.json)
```

### Testing Email in Production

1. Login to the application
2. Go to "Circulation Desk" (Checkout/Issue page)
3. Issue a book to a student
4. Click "Send Reminder" button
5. Check student's email for reminder

---

**Last Updated**: January 2026  
**Framework**: Spring Boot 3.2 with Gmail SMTP
