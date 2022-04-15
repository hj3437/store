const BASE_URL = 'https://dosorme.ga/api/restaurants'

export const request = async (restaurantId, itemsText, itemId) => {
    try {
        const restaurantUrl = restaurantId ? `/${restaurantId}` : ''        
        
        let itemStr = ''        
        if (restaurantUrl) {
            itemStr = itemsText ? `/${itemsText}` : ''
        }
        
        let itemIdStr = ''        
        if (itemsText) {
            itemIdStr = itemId ? `/${itemId}` : ''
        }         
        const requestUrl = `${BASE_URL}${restaurantUrl}${itemStr}${itemIdStr}`    
        
        const response = await fetch(requestUrl)
        if (response.ok) {
            return response.json()
        }
        throw new Error('Network error')
    } catch (e) {
        alert(e.message)
    }
}