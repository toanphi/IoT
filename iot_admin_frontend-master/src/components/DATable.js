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
                message.success("lưu thành công")
            })
            .catch(error => message.error("lưu thất bại"));
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
                message.success("lưu thành công")
            })
            .catch(error => message.error("lưu thất bại"));
    }

    tableColumns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Tên',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Nguyên mẫu',
            dataIndex: 'model',
            key: 'model',  
        },
        {
            title: 'Loại thiết bị',
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
            title: 'Loại switch',
            dataIndex: 'gangs',
            key: 'gangs',
            width: 5
        },
        {
            title: 'Ngày tạo',
            dataIndex: 'createdDate',
            key: 'createdDate',
            width: 170  
        },
        {
            title: 'Mô tả',
            dataIndex: 'desc',
            key: 'desc',
        },
        {
            title: 'Trạng thái',
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
            title: 'Tương tác',
            key: 'action',
            width: 130,  
            render: (text, record) => (
                <Space size="middle">
                    {/* <a href="/" style={{ width: "30%" }} onClick={e => {
                        e.preventDefault();
                        this.onRecordModify(record);
                        this.props.addBtnOnClick(e)
                    }}
                    >Chỉnh sửa</a> */}
                    <Popconfirm
                        title="Bạn thực sự muốn xóa thiết bị này?"
                        onConfirm={this.confirm}
                        onCancel={this.cancel}
                        okText="Có"
                        cancelText="Không"
                    >
                        <a href="/" onClick={e => {
                            e.preventDefault();
                            this.onRecordModify(record)
                        }}>Xóa</a>
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
                    message.success("xóa thành công")
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                }
            })
            .catch(error => message.error("xóa thất bại"));

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