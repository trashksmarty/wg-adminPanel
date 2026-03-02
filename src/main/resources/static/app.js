// Базовые URL нашего backend API.
const tunnelsApi = '/api/admin/tunnels';
const prefixesApi = '/api/admin/prefixes';

// Получаем ссылки на основные DOM-элементы.
const createForm = document.getElementById('create-form');
const refreshBtn = document.getElementById('refresh');
const body = document.getElementById('tunnel-body');
const template = document.getElementById('tunnel-row-template');
const prefixSelect = document.getElementById('prefix-select');

// Обработка формы создания туннеля.
createForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  const formData = new FormData(createForm);

  // Формируем payload для backend.
  const payload = {
    prefix: formData.get('prefix'),
    name: formData.get('name'),
    expiresAt: new Date(formData.get('expiresAt')).toISOString(),
  };

  const response = await fetch(tunnelsApi, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    alert('Ошибка создания туннеля');
    return;
  }

  // После создания очищаем форму и обновляем данные на экране.
  createForm.reset();
  await loadPrefixes();
  await loadTunnels();
});

// Кнопка ручного обновления всех данных.
refreshBtn.addEventListener('click', async () => {
  await loadPrefixes();
  await loadTunnels();
});

// Загружает список префиксов из backend и заполняет select.
async function loadPrefixes() {
  const response = await fetch(prefixesApi);
  if (!response.ok) {
    alert('Не удалось загрузить префиксы');
    return;
  }

  const prefixes = await response.json();
  prefixSelect.innerHTML = '';

  for (const item of prefixes) {
    const option = document.createElement('option');
    option.value = item.prefix;
    option.textContent = item.prefix;
    prefixSelect.appendChild(option);
  }
}

// Загружает список туннелей и перерисовывает таблицу.
async function loadTunnels() {
  const response = await fetch(tunnelsApi);
  if (!response.ok) {
    alert('Не удалось загрузить туннели');
    return;
  }

  const tunnels = await response.json();
  body.innerHTML = '';

  for (const tunnel of tunnels) {
    // Клонируем шаблон строки.
    const fragment = template.content.cloneNode(true);
    const row = fragment.querySelector('tr');

    // Подставляем данные.
    row.querySelector('[data-field="id"]').textContent = tunnel.id;
    row.querySelector('[data-field="name"]').textContent = tunnel.name;
    row.querySelector('[data-field="externalTunnelId"]').textContent = tunnel.externalTunnelId;

    // Показываем статус с разным цветом.
    const statusCell = row.querySelector('[data-field="active"]');
    statusCell.textContent = tunnel.active ? 'Включен' : 'Отключен';
    statusCell.className = tunnel.active ? 'status-enabled' : 'status-disabled';

    // Вставляем текущее значение expiresAt в локальном формате.
    const expiresInput = row.querySelector('[data-field="expiresAtInput"]');
    expiresInput.value = toLocalDateTime(tunnel.expiresAt);

    // Обработчик кнопки сохранения нового срока.
    row.querySelector('[data-action="save-expire"]').addEventListener('click', async () => {
      await updateExpiration(tunnel.id, expiresInput.value);
      await loadTunnels();
    });

    // Обработчик кнопки toggle включить/выключить.
    const toggleBtn = row.querySelector('[data-action="toggle"]');
    toggleBtn.textContent = tunnel.active ? 'Выключить' : 'Включить';
    toggleBtn.classList.toggle('btn-outline-warning', tunnel.active);
    toggleBtn.classList.toggle('btn-outline-success', !tunnel.active);

    toggleBtn.addEventListener('click', async () => {
      await setState(tunnel.id, !tunnel.active);
      await loadTunnels();
    });

    body.appendChild(fragment);
  }
}

// Обновляет дату окончания активности туннеля.
async function updateExpiration(id, localDateTime) {
  const response = await fetch(`${tunnelsApi}/${id}/expiration`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ expiresAt: new Date(localDateTime).toISOString() }),
  });

  if (!response.ok) {
    alert('Не удалось обновить дату');
  }
}

// Переключает состояние туннеля (active/inactive).
async function setState(id, active) {
  const response = await fetch(`${tunnelsApi}/${id}/state/${active}`, { method: 'PATCH' });
  if (!response.ok) {
    alert('Не удалось изменить состояние туннеля');
  }
}

// Преобразует ISO-дату в формат, который понимает input[type=datetime-local].
function toLocalDateTime(isoDate) {
  const date = new Date(isoDate);
  const pad = (n) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`
      + `T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

// Начальная загрузка данных при открытии страницы.
(async () => {
  await loadPrefixes();
  await loadTunnels();
})();
