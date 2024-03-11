import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';

interface Task {
  id: number;
  title: string;
  description: string;
}

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);

  useEffect(() => {
    fetch('/api/tasks')
      .then((response) => response.json())
      .then(setTasks);
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Tasks</h1>
        <ul>
          {tasks.map((task) => (
            <li key={task.id}>{task.title}: {task.description}</li>
          ))}
        </ul>
      </header>
    </div>
  );
}

export default App;
