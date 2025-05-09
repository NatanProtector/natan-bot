# Natan Bot

A multi-functional chatbot application that provides weather information for Israeli cities, tells jokes, and searches for keywords in top-rated movies on IMDB.

## 🤖 Live Demo

Try the bot in action: [Natan Bot Demo](https://natan-bot-9upj.onrender.com)

## 📋 Overview

Natan Bot is a web-based chatbot application built with Spring Boot and integrated with Google's Dialogflow. The chatbot provides three main functionalities:

1. **Weather Service**: Retrieves and delivers current weather information for cities in Israel
2. **Joke Service**: Tells jokes to entertain users
3. **IMDB Service**: Searches for keywords in top-rated movies from IMDB's top 250 list
4. **Banter and chat**: Engage in fun, lighthearted banter with Natan Bot

The application demonstrates web scraping techniques, API integration, and natural language processing capabilities.

## 🛠️ Tech Stack

- **Backend**: Java with Spring Boot
- **NLP**: Google Dialogflow
- **HTTP Client**: OkHttp3
- **API Documentation**: Swagger/OpenAPI
- **Containerization**: Docker
- **Deployment**: Render

## 🚀 Getting Started

### Using Docker

The easiest way to run Natan Bot is using Docker:

```bash
# Run with Docker Compose
docker-compose up
```

You can also download the docker image and run it without cloning the repo

Docker Image: [natanprotector/natan-bot-server](https://hub.docker.com/r/natanprotector/natan-bot-server)

### Running Locally

Alternatively, you can run the application directly from the source code:

1. Clone this repository
2. Navigate to the project directory
3. Start the application from the main class:
    - Run `ChatbotApplication.java`

### API Documentation

Once the server is running, you can explore the available endpoints using Swagger UI:

```
http://localhost:8080/swagger-ui/
```

## 📝 Important Note

The chat functionality on the web interface is linked to a private Dialogflow account, so it won't work if you run the application locally. To fully utilize the chatbot interface:

1. Create your own Dialogflow agent
2. Update the agent ID in the `chat.html` file
3. Configure intents in Dialogflow to work with the provided endpoints

For detailed API documentation, refer to the Swagger UI when the application is running.

## 📦 Deployment

The application is deployed on Render and can be accessed at [https://natan-bot-9upj.onrender.com](https://natan-bot-9upj.onrender.com).

## **Acknowledgments**

This project was developed as part of the Hands-On Programming course by Niv Itzhaki.
Through this experience, I had the opportunity to explore many amazing technologies and gain practical knowledge in backend development, security, testing, and DevOps.
Huge thanks to Niv Itzhaki for the guidance and support throughout the course!