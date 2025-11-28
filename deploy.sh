#!/bin/bash

set -e

echo "Starting deployment..."

# Stop existing container
if [ "$(docker ps -q -f name=jjigit-backend)" ]; then
    echo "Stopping existing container..."
    docker stop jjigit-backend
fi

# Remove existing container
if [ "$(docker ps -aq -f name=jjigit-backend)" ]; then
    echo "Removing existing container..."
    docker rm jjigit-backend
fi

# Remove old images (keep latest)
echo "Cleaning up old images..."
docker image prune -f

# Start new container
echo "Starting new container..."
docker-compose up -d jjigit-app

# Wait for application to be healthy
echo "Waiting for application to be healthy..."
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
    if docker exec jjigit-backend curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo "Application is healthy!"
        break
    fi

    attempt=$((attempt + 1))
    echo "Attempt $attempt/$max_attempts: Application not ready yet..."
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "Application failed to become healthy"
    docker logs jjigit-backend --tail 50
    exit 1
fi

echo "Deployment completed successfully!"

# Show logs
echo "Recent logs:"
docker logs jjigit-backend --tail 20
