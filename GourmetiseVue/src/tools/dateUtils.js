function formatDate(date) {
    let formattedDate = new Date(date).toLocaleDateString('fr-FR', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  
    formattedDate = formattedDate.replace(/([a-zA-ZéÈêà]{3})\./, '$1');
    
    return formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);
  }

  export default formatDate ;