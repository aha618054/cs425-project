# Use the official Node.js image as the base image
FROM node:18-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the package.json and package-lock.json to install dependencies first
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the React app source code into the container
COPY . .

# Build the React app
RUN npm run build

# Serve the React app with a simple static file server
RUN npm install -g serve

# Expose the port React app will run on
EXPOSE 3000

# Command to run the React app
CMD ["serve", "-s", "build", "-l", "3000"]
