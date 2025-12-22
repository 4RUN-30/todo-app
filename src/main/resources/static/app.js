async function addTodo() {
  const input = document.getElementById("todoInput");
  const title = input.value.trim();

  if (!title) return;

  await fetch("/todos", {
    method: "POST",
    headers: { "Content-Type": "text/plain" },
    body: title
  });

  input.value = "";
  loadTodos();
}

async function loadTodos() {
  const res = await fetch("/todos");
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
  await fetch(`/todos/${id}/complete`, {
    method: "PUT"
  });
  loadTodos();
}

async function removeTodo(id) {
  await fetch(`/todos/${id}`, {
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
