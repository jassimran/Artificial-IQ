# Artificial-IQ 
## An AI agent to solve [Raven’s Progressive Matrices Problems](https://en.wikipedia.org/wiki/Raven%27s_Progressive_Matrices) (A standard non-verbal intelligence test)

My agent uses OpenCV library for recognizing the images, Semantic Networks for knowledge representation and Generate & Test method to manipulate the data. Details:

1. **OpenCV library :** I thought of leveraging the amazing features and variety of algorithms available in OpenCV to recognize the images. 

2. **Semantic Networks :** The reason behind choosing Semantic Networks is its simplicity, its ability to make the relationships explicit, transparency and speed as it doesn’t save extraneous data and still has high computability. 

3. **Generate & Test method:** THis method first guesses the solution and then tests whether this solution is correct and satisfies the constraints. Due to limited resources, my agent uses Smart Generator which is well-informed and generates the best possible solution. The tester then tests the solution against every option. This increases performance of the Agent.

4. **Programming Language:** Java has been used as the language of programming of the agent.

Please refer to the [report](https://github.com/jassimran/Artificial-IQ/blob/master/Project%20Report%204.docx) for detailed  approach and analysis
