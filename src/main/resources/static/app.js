async function addTodo() {
  const input = document.getElementById("todoInput");
  const title = input.value.trim();

  if (!title) return;

  // Send a JSON object matching the Todo controller's @RequestBody Todo parameter
  await fetch("/todo", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ title })
  });

  input.value = "";
  loadTodos();
}

async function loadTodos() {
  const res = await fetch("/todo");
  const todos = await res.json();

  document.getElementById("output").innerHTML =
    todos.map(t =>
      `<div class="todo">
          <span class="${t.completed ? 'completed' : ''}">
              ${t.completed ? '✅' : '⬜'} ${t.title}
          </span>

          ${!t.completed
            ? `<button onclick="complete('${t.id}')">Done</button>`
            : `<button onclick="removeTodo('${t.id}')">❌</button>`
          }
       </div>`
    ).join("");
}

async function complete(id) {
  // The backend expects a full Todo object on PUT /todo/{id}.
  // Fetch the current todo, mark it completed and PUT the updated object.
  try {
    const res = await fetch(`/todo/${id}`);
    if (!res.ok) throw new Error('Failed to fetch todo');
    const todo = await res.json();
    todo.completed = true;
    todo.completedAt = new Date().toISOString();

    await fetch(`/todo/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(todo)
    });
  } catch (e) {
    console.error('Could not complete todo', e);
  }
  loadTodos();
}

async function removeTodo(id) {
  await fetch(`/todo/${id}`, {
    method: "DELETE"
  });
  loadTodos();
}

// Initial load
loadTodos();

// PWA Service Worker
if ("serviceWorker" in navigator) {
  navigator.serviceWorker
    .register("/service-worker.js")
    .then(() => console.log("Service Worker registered"));
}
