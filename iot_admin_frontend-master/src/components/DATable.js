import { Table, Popconfirm, Space, message, Switch } from 'antd';
import ManageDeviceForm from '../pages/ManageDeviceForm';
import React from 'react';
import '../App.css'

class DATable extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            tableData: [],
            pageSize: "5",
            modifyRecord: false,
            sort: this.props.sort,
            lstSupportedDevice: []
        }
    }
    componentDidMount() {
        try {
            this.fetchTableData()
            this.fetchSupportedDevice()
        } catch (e) {
            console.log(e)
        }
        
    }

    fetchSupportedDevice = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/supported/get/all`, requestOptions)
            .then(response => {
                if (response.status === 200) 
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                this.setState({
                    lstSupportedDevice: data.data
                }) 
            })
            .catch(error => console.log('error', error));
    }

    fetchTableData = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device`, requestOptions)
            .then(response => {
                if (response.status === 200) 
                    return response.json()
                else if (response.status === 401 || response.status === 403) {
                    console.log(123123123)
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                console.log(data)
                let newData = data.data
                for (let device in newData) {
                    newData[device].key = newData[device].name
                }
                this.props.onRecordNumsChange(newData.length)
                this.setState({
                    tableData: newData
                })  
            })
            .catch(error => console.log('error', error));
    }

    fetchSaveDevice = (device) => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        redirect: 'follow',
        body: JSON.stringify(device)
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device`, requestOptions)
            .then(response => {
                
                if (response.status === 200) {
                    return response.json()
                } else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                this.props.addBtnOnClick()
                this.onAddRecord(device)
                message.success("l??u th??nh c??ng")
            })
            .catch(error => message.error("l??u th???t b???i"));
    }

    onStatusChange = (device, switchValue) => {
        device.status = switchValue

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        redirect: 'follow',
        body: JSON.stringify(device)
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device`, requestOptions)
            .then(response => {                
                if (response.status === 200) {
                    return response.json()
                } else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                this.onAddRecord(device)
                message.success("l??u th??nh c??ng")
            })
            .catch(error => message.error("l??u th???t b???i"));
    }

    tableColumns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'T??n',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Nguy??n m???u',
            dataIndex: 'model',
            key: 'model',  
        },
        {
            title: 'Lo???i thi???t b???',
            dataIndex: 'productType',
            key: 'productType',
            width: 100,
            filters: [
                {
                    text: 'sensor',
                    value: 'sensor',
                },
                {
                    text: 'remote',
                    value: 'remote',
                },
                {
                    text: 'switch',
                    value: 'switch',
                }
            ],
            onFilter: (value, record) => record.productType.indexOf(value) === 0,
        },
        {
            title: 'Control Topic',
            dataIndex: 'controlTopic',
            key: 'controlTopic',
        },
        {
            title: 'Output Topic',
            dataIndex: 'outputTopic',
            key: 'outputTopic',
        },
        {
            title: 'Lo???i switch',
            dataIndex: 'gangs',
            key: 'gangs',
            width: 5
        },
        {
            title: 'Ng??y t???o',
            dataIndex: 'createdDate',
            key: 'createdDate',
            width: 170  
        },
        {
            title: 'M?? t???',
            dataIndex: 'desc',
            key: 'desc',
        },
        {
            title: 'Tr???ng th??i',
            dataIndex: 'status',
            key: 'status',
            width: 70,
            render: (value, record) => (
                <Switch defaultChecked={value} onChange={(switchValue) => {
                    this.onStatusChange(record, switchValue)
                }} />
            )
        },
        {
            title: 'T????ng t??c',
            key: 'action',
            width: 130,  
            render: (text, record) => (
                <Space size="middle">
                    {/* <a href="/" style={{ width: "30%" }} onClick={e => {
                        e.preventDefault();
                        this.onRecordModify(record);
                        this.props.addBtnOnClick(e)
                    }}
                    >Ch???nh s???a</a> */}
                    <Popconfirm
                        title="B???n th???c s??? mu???n x??a thi???t b??? n??y?"
                        onConfirm={this.confirm}
                        onCancel={this.cancel}
                        okText="C??"
                        cancelText="Kh??ng"
                    >
                        <a href="/" onClick={e => {
                            e.preventDefault();
                            this.onRecordModify(record)
                        }}>X??a</a>
                    </Popconfirm>
                </Space>
            ),
        },
    ];


    /** 
     * Modify record functions
     */
    onRecordModify = (record) => {
        this.setState({
            modifyRecord: record
        })
    }

    // clear state
    clearModifyRecord = () => {
        this.setState({
            modifyRecord: false
        })
    }

    onAddRecord = (device) => {
        let newDevice = Object.assign({}, device)
        newDevice.key = device.name

        let list = this.state.tableData.filter((item) => { return (item.name !== newDevice.name) })
        list.push(newDevice)
        this.setState({
            tableData: list
        })

        this.props.onRecordNumsChange(list.length)
    }

    confirm = (e) => {
        let tmpDevice = this.state.tableData.filter((item) => { return (item.name === this.state.modifyRecord.name) })[0]

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'DELETE',
        headers: myHeaders,
        redirect: 'follow',
        };

        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/${tmpDevice.id}`, requestOptions)
            .then(response => {                
                if (response.status === 200) 
                    message.success("x??a th??nh c??ng")
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                }
            })
            .catch(error => message.error("x??a th???t b???i"));

        let list = this.state.tableData.filter((item) => { return (item.name !== this.state.modifyRecord.name) })
        this.setState({
            tableData: list
        })
        this.props.onRecordNumsChange(list.length)
        this.clearModifyRecord()
    }

    cancel = (e) => {
    }

    render() {
        return <div>
            <Table
                columns={this.tableColumns}
                dataSource={this.state.tableData}
                style={{}}
                pagination={{ pageSize: this.props.pageSize }}
                bordered
            // scroll={{ y: '100%' }}
            />
            <div className={this.props.isAddForm ? "manage-form manage-form-on" : "manage-form manage-form-off"} style={{}}>
                {!this.props.isAddFormAnimation ?
                    <ManageDeviceForm
                        onClose={this.props.addBtnOnClick}
                        modifyRecord={this.state.modifyRecord}
                        clearModifyRecord={this.clearModifyRecord}
                        onAddRecord={this.onAddRecord}
                        lstSupportedDevice={this.state.lstSupportedDevice}
                        fetchSaveDevice={this.fetchSaveDevice}
                    />
                    : null}
            </div>
        </div>;
    }
}



export default DATable;