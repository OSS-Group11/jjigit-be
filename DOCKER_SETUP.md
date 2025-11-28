# Docker CI/CD Setup Guide

This guide will help you configure automated deployment to EC2 using Docker and GitHub Actions.

## Prerequisites

- EC2 instance running
- PEM key file for SSH access
- GitHub repository with admin access
- AWS RDS database already configured

## Step 1: EC2 Initial Setup

SSH into your EC2 instance and run the following commands:

```bash
# Update system
sudo apt-get update
sudo apt-get upgrade -y

# Install Docker
sudo apt-get install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Add current user to docker group
sudo usermod -aG docker $USER

# Install curl (for health checks)
sudo apt-get install -y curl

# Logout and login again for group changes to take effect
exit
```

## Step 2: Configure GitHub Secrets

Go to your GitHub repository: **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

Add the following secrets:

| Secret Name | Value Description | Example Format |
|------------|-------------------|----------------|
| `EC2_HOST` | EC2 instance public IP address | `12.34.56.78` |
| `EC2_USER` | EC2 username (depends on AMI) | `ubuntu` or `ec2-user` |
| `EC2_PEM_KEY` | Complete content of your PEM key file | Copy entire `.pem` file content |
| `PROD_DB_HOST` | RDS endpoint hostname | `your-db.region.rds.amazonaws.com` |
| `PROD_DB_PORT` | Database port | `3306` |
| `PROD_DB_NAME` | Database name | `your_database_name` |
| `PROD_DB_USERNAME` | Database username | `admin` |
| `PROD_DB_PASSWORD` | Database password | Your secure RDS password |
| `JWT_SECRET` | JWT secret key (min 256 bits) | Use a strong random string |
| `JWT_EXPIRATION` | JWT expiration time in milliseconds | `86400000` (24 hours) |
| `PROD_SERVER_URL` | Your server base URL | `http://YOUR_EC2_IP:8080` |
| `DB_PASSWORD` | Local MySQL password (dev only) | Any password for local dev |

### How to add EC2_PEM_KEY:

1. Open your PEM file in a text editor
2. Copy the **entire content** including `-----BEGIN RSA PRIVATE KEY-----` and `-----END RSA PRIVATE KEY-----`
3. Paste it as the value for `EC2_PEM_KEY` secret

## Step 3: Verify EC2 Security Group

Make sure your EC2 security group allows the following inbound traffic:

- **Port 8080**: HTTP (Application)
- **Port 22**: SSH (Deployment)

## Step 4: Test Deployment

1. Push changes to the `main` branch
2. Go to **Actions** tab in GitHub repository
3. Watch the deployment workflow run
4. Once completed, verify the application is running:

```bash
curl http://YOUR_EC2_IP:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

## Step 5: Access Your Application

- **API Base URL**: `http://YOUR_EC2_IP:8080/api`
- **Swagger UI**: `http://YOUR_EC2_IP:8080/swagger-ui.html`
- **Health Check**: `http://YOUR_EC2_IP:8080/actuator/health`

## Workflow Overview

1. Developer pushes code to `main` branch
2. GitHub Actions automatically:
   - Builds the application with Gradle
   - Creates Docker image
   - Transfers files to EC2
   - Deploys new container
   - Performs health check
3. Old container is stopped and removed
4. New container starts serving traffic

## Troubleshooting

### Check application logs on EC2:
```bash
ssh -i your-key.pem ubuntu@YOUR_EC2_IP
docker logs jjigit-backend
```

### Restart application manually:
```bash
cd /home/ubuntu/jjigit
docker-compose restart jjigit-app
```

### Check running containers:
```bash
docker ps
```

### View deployment script logs:
```bash
cd /home/ubuntu/jjigit
cat deploy.log
```

## Notes

- Deployment happens automatically on every push to `main`
- Each deployment performs a health check before completing
- Failed deployments will show container logs for debugging
- All environment variables are securely stored in GitHub Secrets
- **Never commit sensitive information (passwords, keys, IPs) to the repository**
