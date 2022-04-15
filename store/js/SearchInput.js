export default function SearchInput({ $header, initState, onSearch }) {
    this.state = initState
    this.onSearch = onSearch

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {
    }

    this.render()

    $header.addEventListener('change', async (e) => {
        if (e.target.tagName === 'INPUT') {            
            this.preSearch(e.target.value)
        }
    })

    $header.querySelector('.header-search-button').addEventListener('click', (e) => {
        const { value } = $header.querySelector('input')        
        this.preSearch(value)
    })

    this.preSearch = (value) => {
        this.onSearch(value.trim())
    }
}