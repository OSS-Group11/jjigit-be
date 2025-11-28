#!/bin/bash

set -e

echo "Starting deployment..."

# Stop and remove existing container
docker-compose down || true

# Start new container
docker-compose up -d jjigit-app

echo "Deployment completed!"
echo "Application is starting..."

# Show logs
docker logs jjigit-backend --tail 30 --follow &
sleep 10
kill $! 2>/dev/null || true

echo "Done! Check logs with: docker logs jjigit-backend"
