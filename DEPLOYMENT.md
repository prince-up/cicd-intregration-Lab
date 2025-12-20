# 🚀 Deployment Guide

This project is configured for cloud deployment.
- **Frontend**: Vercel (Recommended) or Netlify
- **Backend**: Render, Railway, or Heroku (using Docker)

---

## 🏗️ 1. Deploying the Backend (API)

The backend is containerized with Docker, making it easy to deploy on any platform that supports Docker.

### Option A: Railway (Easiest)
1. Go to [Railway.app](https://railway.app/) and sign up.
2. Click **New Project** -> **Deploy from GitHub repo**.
3. Select your repository.
4. **Important**: Railway might try to deploy both folders. You need to configure it to deploy the `backend` folder.
    - Go to **Settings** -> **Root Directory** and set it to `/backend`.
5. Railway will automatically detect the `Dockerfile` and build it.
6. Once deployed, copy the **Public Domain** (e.g., `https://student-cicd-production.up.railway.app`).

### Option B: Render
1. Go to [Render.com](https://render.com/).
2. Create a new **Web Service**.
3. Connect your GitHub repository.
4. Settings:
    - **Root Directory**: `backend`
    - **Runtime**: Docker
5. Click **Deploy**.
6. Copy the service URL once it's live.

---

## 🎨 2. Deploying the Frontend (React)

The frontend is a standard React app.

### Steps for Vercel:
1. Go to [Vercel.com](https://vercel.com/new).
2. Import your GitHub repository.
3. Configure the Project:
    - **Root Directory**: Click "Edit" and select `frontend`.
    - **Framework Preset**: Create React App (should be auto-detected).
4. **Environment Variables**:
    - Add a new variable:
        - Name: `REACT_APP_API_URL`
        - Value: `<YOUR_BACKEND_URL_FROM_STEP_1>`
        - Example: `https://student-cicd-production.up.railway.app/api/pipeline` **(Note: Append `/api/pipeline`!)**
5. Click **Deploy**.

---

## ✅ 3. Verify Deployment

1. Open your Vercel URL.
2. The Dashboard should load (showing "Pipeline Dashboard").
3. If it says "Failed to fetch executions", check the Console (F12) to ensure it's trying to reach your Cloud Backend URL, not localhost.
4. Try triggering a new pipeline!

> **Note:** Since the Jenkins server (which runs the actual builds) is likely still running on your **local machine**, the Cloud Backend won't be able to trigger it unless you expose your local Jenkins to the internet (using ngrok) or deploy Jenkins to the cloud too. For this demo, the "Dashboard" and "History" will work, but the "Trigger" might fail or timeout on the cloud version if it can't reach Jenkins.
