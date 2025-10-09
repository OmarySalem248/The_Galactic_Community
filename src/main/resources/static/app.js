let selectedColonistId = null;

async function fetchColonists() {
    const res = await fetch('/api/colonists');
    const colonists = await res.json();
    const dropdown = document.getElementById('colonistDropdown');
    dropdown.innerHTML = '';
    colonists.forEach(c => {
        const option = document.createElement('option');
        option.value = c.id;
        option.text = c.name;
        dropdown.add(option);
    });
    if (colonists.length > 0) {
        selectedColonistId = colonists[0].id;
        updateColonistStats();
        updateRelationships();
    }
}

async function updateColonistStats() {
    if (!selectedColonistId) return;
    const res = await fetch(`/api/colonists/${selectedColonistId}`);
    const c = await res.json();
    document.getElementById('ageLabel').innerText = `${c.age} Years`;
    document.getElementById('occupationLabel').innerText = c.occupation;
    document.getElementById('energyLabel').innerText = c.energy;
    document.getElementById('hpLabel').innerText = c.hp;
    document.getElementById('buildingLabel').innerText = c.assignedBuilding || 'Unassigned';
    document.getElementById('professionLabel').innerText = c.profession;
}

async function updateRelationships() {
    if (!selectedColonistId) return;
    const res = await fetch(`/api/colonists/${selectedColonistId}/relationships`);
    const rels = await res.json();
    const tbody = document.getElementById('relationshipTable').querySelector('tbody');
    tbody.innerHTML = '';
    rels.forEach(r => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${r.name}</td>
            <td>${r.ROMANTIC}</td>
            <td>${r.FRIENDSHIP}</td>
            <td>${r.FAMILY}</td>
            <td>${r.PROXIMITY}</td>
        `;
        tbody.appendChild(row);
    });
}

document.getElementById('colonistDropdown').addEventListener('change', (e) => {
    selectedColonistId = e.target.value;
    updateColonistStats();
    updateRelationships();
});

document.getElementById('nextTurnBtn').addEventListener('click', async () => {
    await fetch('/api/turn/next', { method: 'POST' });
    fetchColonists();
});


document.getElementById('feedBtn').addEventListener('click', async () => {
    await fetch(`/api/colonists/${selectedColonistId}/feed?amount=1`, { method: 'POST' });
    updateColonistStats();
});
document.getElementById('reduceFeedBtn').addEventListener('click', async () => {
    await fetch(`/api/colonists/${selectedColonistId}/feed?amount=-1`, { method: 'POST' });
    updateColonistStats();
});

fetchColonists();
